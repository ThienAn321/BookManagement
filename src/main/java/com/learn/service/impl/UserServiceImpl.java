package com.learn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.exception.EmailNotFoundException;
import com.learn.exception.LinkVerifyNotFound;
import com.learn.model.VerificationToken;
import com.learn.model.User;
import com.learn.model.enumeration.Role;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.service.VerificationTokenService;
import com.learn.service.UserService;
import com.learn.service.dto.RegisterDTO;

@Service
public class UserServiceImpl implements UserService {

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
//        LocalDateTime now = LocalDateTime.now();
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

        if (!confirmToken.isPresent() || confirmToken.get().isExpired()) {
            throw new LinkVerifyNotFound("Link verify hoặc otp không tồn tại hoặc đã hết hạn");
        }

        Optional<User> user = userRepository.findByEmail(confirmToken.get().getUser().getEmail());

        if (!user.isPresent()) {
            throw new EmailNotFoundException("Không tìm thấy email");
        }

        user.get().setUserStatus(UserStatus.ACTIVATED);
        userRepository.save(user.get());
        verificationTokenService.delete(confirmToken.get().getId());
        return true;
    }

}
