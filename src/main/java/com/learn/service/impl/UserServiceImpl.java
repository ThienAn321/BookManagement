package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.exception.EmailAlreadyActived;
import com.learn.exception.EmailNotFoundException;
import com.learn.exception.LinkVerifyIsNotExpired;
import com.learn.exception.LinkVerifyNotFound;
import com.learn.exception.UserInvalidException;
import com.learn.model.VerificationToken;
import com.learn.model.User;
import com.learn.model.enumeration.Role;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.service.VerificationTokenService;
import com.learn.service.UserService;
import com.learn.service.dto.AuthenticationRequest;
import com.learn.service.dto.RegisterDTO;
import com.learn.service.dto.RequestRefreshDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final int MAX_FAILED_ATTEMPTS = 3;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void register(RegisterDTO request) {
        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())).fullname(request.getFullname())
                .email(request.getEmail()).phone(request.getPhone()).failedAttempt(0).role(Role.USER)
                .userStatus(UserStatus.NOTACTIVATED).build();
        userRepository.save(user);
        verificationTokenService.sendConfirmationToken(user);
    }

    @Override
    public boolean verifyRegisterAccount(String token, String otp) {
        Optional<VerificationToken> confirmToken = verificationTokenService.findByTokenOrOtp(token, otp);
        boolean isExpire = verificationTokenService.isExpire(token, otp);
        if (!confirmToken.isPresent() || isExpire) {
            throw new LinkVerifyNotFound("Link verify hoặc otp không tồn tại hoặc đã hết hạn");
        }

        Optional<User> user = userRepository.findByEmail(confirmToken.get().getUser().getEmail());

        if (!user.isPresent()) {
            throw new EmailNotFoundException("Không tìm thấy email");
        }

        confirmToken.get().setVerify(true);
        verificationTokenService.save(confirmToken.get());
        user.get().setUserStatus(UserStatus.ACTIVATED);
        userRepository.save(user.get());
//      verificationTokenService.delete(confirmToken.get().getId());
        return true;
    }

    @Override
    public void resendVerify(RequestRefreshDTO request) {
        Optional<User> user = findByEmail(request.getEmail());
        if (!user.isPresent()) {
            throw new EmailNotFoundException("Email không tồn tại");
        }

        if (user.get().getUserStatus().equals(UserStatus.ACTIVATED)) {
            throw new EmailAlreadyActived("Email này đã active");
        }

        VerificationToken verificationToken = user.get().getVerificationToken();
        if (verificationToken != null && (verificationToken.isExpire())) {
            verificationTokenService.delete(verificationToken.getId());
        } else {
            throw new LinkVerifyIsNotExpired("Link chưa hết hạn ! Vui lòng check lại email hoặc gửi lại sau 15 phút !");
        }
        verificationTokenService.sendConfirmationToken(user.get());
    }

    @Override
    @Transactional(dontRollbackOn = UserInvalidException.class)
    public void loginFailed(AuthenticationRequest request, User user) {
        String password = user.getPassword();
        if (passwordEncoder.matches(request.getPassword(), password) && !isLockTimeExpire(user)) {
            resetTime(user);
        } else {
            increaseFailedAttempts(user);
        }
    }

    private void increaseFailedAttempts(User user) {
        if (user.getFailedAttempt() + 1 > MAX_FAILED_ATTEMPTS) {
            if (user.getLockTime() == null) {
                user.setLockTime(Instant.now().plus(30, ChronoUnit.SECONDS));
                user.setUserStatus(UserStatus.DISABLE);
                userRepository.save(user);
            }
            throw new UserInvalidException("Sai mật khẩu ! Acc bạn đã bị khóa 30 phút");
        } else {
            int newIncrease = user.getFailedAttempt() + 1;
            userRepository.updateFailedAttempts(newIncrease, user.getEmail());
            throw new UserInvalidException("Sai mật khẩu !");
        }
    }

    // kiểm tra thời gian lock của user
    private boolean isLockTimeExpire(User user) {
//        System.out.println(user.getLockTime());
//        System.out.println(Instant.now());
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
