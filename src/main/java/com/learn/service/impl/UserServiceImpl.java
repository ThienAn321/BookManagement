package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.exception.DataNotFoundException;
import com.learn.exception.DataUnauthorizedException;
import com.learn.model.VerificationToken;
import com.learn.model.User;
import com.learn.model.enumeration.Role;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.VerificationTokenService;
import com.learn.service.dto.AuthenticationRequestDTO;
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RegisterDTO;
import com.learn.service.UserService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final int MAX_FAILED_ATTEMPTS = 3;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ObjectDTO register(RegisterDTO request) {
        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())).fullname(request.getFullname())
                .email(request.getEmail()).phone(request.getPhone()).failedAttempt(0).role(Role.USER)
                .userStatus(UserStatus.NOTACTIVATED).build();
        userRepository.save(user);
        verificationTokenService.sendVerificationToken(user);
        return ObjectDTO.builder().statusCode(HttpStatus.CREATED.value()).message("{register.success}")
                .timestamp(Instant.now()).build();
    }

    @Override
    @Transactional(dontRollbackOn = DataNotFoundException.class)
    public ObjectDTO verifyRegisterAccount(String token, String otp) {
        Optional<VerificationToken> confirmToken = verificationTokenRepository.findByTokenOrOtp(token, otp);
        boolean isExpire = verificationTokenService.isExpire(token, otp);
        if (!confirmToken.isPresent() || isExpire) {
            throw new DataNotFoundException("{verify.invalid}");
        }

        Optional<User> user = userRepository.findByEmail(confirmToken.get().getUser().getEmail());

        if (!user.isPresent()) {
            throw new DataNotFoundException("{email.notfound}");
        }

        confirmToken.get().setVerify(true);
        verificationTokenRepository.save(confirmToken.get());
        user.get().setUserStatus(UserStatus.ACTIVATED);
        userRepository.save(user.get());
        verificationTokenRepository.deleteById(confirmToken.get().getId());
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{verify.success}")
                .timestamp(Instant.now()).build();
    }

    @Override
    @Transactional(dontRollbackOn = DataUnauthorizedException.class)
    public void loginFailed(AuthenticationRequestDTO request, User user) {
        String password = user.getPassword();
        if (user.getUserStatus().equals(UserStatus.ACTIVATED)) {
            if (passwordEncoder.matches(request.getPassword(), password) && !isLockTimeExpire(user)) {
                resetTime(user);
            } else {
                increaseFailedAttempts(user);
            }
        }
    }

    private void increaseFailedAttempts(User user) {
        if (user.getFailedAttempt() + 1 > MAX_FAILED_ATTEMPTS) {
            if (user.getLockTime() == null) {
                user.setLockTime(Instant.now().plus(30, ChronoUnit.MINUTES));
                user.setUserStatus(UserStatus.DISABLE);
                userRepository.save(user);
            }
            throw new DataUnauthorizedException("{user.lock}");
        } else {
            int newIncrease = user.getFailedAttempt() + 1;
            userRepository.updateFailedAttempts(newIncrease, user.getEmail());
            throw new DataUnauthorizedException("{password.wrong}");
        }
    }

    // kiểm tra thời gian lock của user
    private boolean isLockTimeExpire(User user) {
        if (user.getLockTime() == null) {
            return false;
        }
        return user.getLockTime().compareTo(Instant.now()) > 0;
    }

    private void resetTime(User user) {
        user.setUserStatus(UserStatus.ACTIVATED);
        user.setFailedAttempt(0);
        user.setLockTime(null);
        userRepository.save(user);
    }

}
