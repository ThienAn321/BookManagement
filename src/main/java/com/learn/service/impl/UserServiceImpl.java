package com.learn.service.impl;

import java.time.Instant;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.exception.DataInvalidException;
import com.learn.exception.DataNotFoundException;
import com.learn.model.VerificationToken;
import com.learn.model.User;
import com.learn.model.enumeration.Role;
import com.learn.model.enumeration.TitleToken;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.VerificationTokenService;
import com.learn.service.dto.ChangePasswordDTO;
import com.learn.service.dto.ChangephoneRequestDTO;
import com.learn.service.dto.EmailRequestDTO;
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RegisterDTO;
import com.learn.service.SmsSenderService;
import com.learn.service.UserService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private SmsSenderService smsSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource message;

    @Override
    public ObjectDTO register(RegisterDTO request) {
        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())).fullname(request.getFullname())
                .email(request.getEmail()).phone(request.getPhone()).failedAttempt(0).isDeleted(false).role(Role.USER)
                .userStatus(UserStatus.NOTACTIVATED).build();

        userRepository.save(user);
        verificationTokenService.sendVerificationToken(user, TitleToken.CONFIRM_ACCOUNT);
        logger.info("User {} register successfully at : {}", request.getEmail(), Instant.now());
        return ObjectDTO.builder().statusCode(HttpStatus.CREATED.value())
                .message(message.getMessage("register.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    @Override
    @Transactional(dontRollbackOn = DataNotFoundException.class)
    public ObjectDTO verifyLinkAndOtpConfirmAccount(String token, String otp) {
        User user = verifyLinkOrOtp(token, otp, TitleToken.CONFIRM_ACCOUNT);
        user.setUserStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
        logger.info("User {} verify account success at : {}", user.getEmail(), Instant.now());
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{verify.success}")
                .timestamp(Instant.now()).build();
    }

    @Override
    public ObjectDTO changeEmail(EmailRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new DataNotFoundException(message.getMessage("email.notfound", null, Locale.getDefault())));

        verificationTokenService.sendVerificationToken(user, TitleToken.CHANGE_EMAIL);
        user.setUserStatus(UserStatus.DISABLE);
        userRepository.save(user);
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{sendEmail.success}")
                .timestamp(Instant.now()).build();
    }

    @Override
    @Transactional(dontRollbackOn = DataNotFoundException.class)
    public ObjectDTO verifyChangeEmail(String token, String otp, EmailRequestDTO request) {
        User user = verifyLinkOrOtp(token, otp, TitleToken.CHANGE_EMAIL);
        if (user.getEmail().equals(request.getEmail())) {
            throw new DataInvalidException(message.getMessage("changeemail.invalid", null, Locale.getDefault()));
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DataInvalidException("This email already exits");
        }

        user.setUserStatus(UserStatus.ACTIVATED);
        user.setEmail(request.getEmail());
        userRepository.save(user);
        logger.info("User {} verify change email success at : {}", user.getEmail(), Instant.now());
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{verify.success}")
                .timestamp(Instant.now()).build();
    }

    @Override
    public ObjectDTO resetPassword(EmailRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new DataNotFoundException(message.getMessage("email.notfound", null, Locale.getDefault())));
        String newPassword = verificationTokenService.sendResetPassword(user);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUserStatus(UserStatus.DISABLE);
        userRepository.save(user);
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{sendEmail.success}")
                .timestamp(Instant.now()).build();
    }

    @Override
    @Transactional(dontRollbackOn = DataNotFoundException.class)
    public ObjectDTO verifyResetPassword(String token, String otp, ChangePasswordDTO request, String email) {
        User user = verifyLinkOrOtp(token, otp, TitleToken.RESET_PASSWORD);
        user.setUserStatus(UserStatus.ACTIVATED);
        checkPassword(request, email);
        userRepository.save(user);
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{reset success}").timestamp(Instant.now())
                .build();
    }

    @Override
    public ObjectDTO changePassword(ChangePasswordDTO request, Authentication authentication) {
        String email = authentication.getName();
        checkPassword(request, email);
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value())
                .message(message.getMessage("changepassword.success", null, Locale.getDefault()))
                .timestamp(Instant.now()).build();
    }

    @Override
    public ObjectDTO changePhone(ChangephoneRequestDTO request) {
        String test = smsSenderService.sendSMS(request);
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{test from twilio}")
                .timestamp(Instant.now()).build();
    }

    private User verifyLinkOrOtp(String token, String otp, TitleToken typeEmail) {
        VerificationToken confirmToken = verificationTokenRepository.findByVerifyToken(token, otp, typeEmail)
                .orElseThrow(() -> new DataInvalidException("not found"));

        if (verificationTokenService.isExpire(token, otp, typeEmail)) {
            verificationTokenRepository.deleteById(confirmToken.getId());
            logger.warn("Verification token has been expired");
            throw new DataNotFoundException(message.getMessage("verify.invalid", null, Locale.getDefault()));
        }

        User user = userRepository.findByEmail(confirmToken.getUser().getEmail()).orElseThrow(
                () -> new DataNotFoundException(message.getMessage("email.notfound", null, Locale.getDefault())));
        verificationTokenRepository.deleteById(confirmToken.getId());
        return user;
    }

    private void checkPassword(ChangePasswordDTO request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new DataNotFoundException(message.getMessage("email.notfound", null, Locale.getDefault())));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            logger.warn("Old password is not match with user password in database");
            throw new DataInvalidException(message.getMessage("password.notmatch", null, Locale.getDefault()));
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            logger.warn("New password is the same with old password");
            throw new DataInvalidException(message.getMessage("password.same", null, Locale.getDefault()));
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            logger.warn("Confirm password is not match with new password");
            throw new DataInvalidException(message.getMessage("newpassword.invalid", null, Locale.getDefault()));
        }

        user.setPassword(passwordEncoder.encode(request.getConfirmNewPassword()));
        userRepository.save(user);
        logger.info("User {} change password successfully at : {}", user.getEmail(), Instant.now());
    }

}
