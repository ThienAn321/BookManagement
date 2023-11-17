package com.learn.service.impl;

import java.time.Instant;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.exception.DataInvalidException;
import com.learn.exception.DataNotFoundException;
import com.learn.model.User;
import com.learn.model.VerificationToken;
import com.learn.model.enumeration.Role;
import com.learn.model.enumeration.TitleToken;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.UserService;
import com.learn.service.VerificationTokenService;
import com.learn.service.dto.UserDTO;
import com.learn.service.dto.request.ChangePasswordDTO;
import com.learn.service.dto.request.ChangePhoneRequestDTO;
import com.learn.service.dto.request.EmailRequestDTO;
import com.learn.service.dto.response.MessageDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final VerificationTokenService verificationTokenService;

    private final PasswordEncoder passwordEncoder;

    private final MessageSource message;

    @Override
    public MessageDTO register(UserDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Email {} failed to register at : {}", request.getEmail(), Instant.now());
            throw new DataInvalidException("email", message.getMessage("email.invalid", null, Locale.getDefault()),
                    "email.error.invalid");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("Email {} failed to register at : {}", request.getEmail(), Instant.now());
            throw new DataInvalidException("username",
                    message.getMessage("username.invalid", null, Locale.getDefault()), "username.error.invalid");
        }

        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            logger.warn("Email {} failed to register at : {}", request.getEmail(), Instant.now());
            throw new DataInvalidException("phone", message.getMessage("phone.invalid", null, Locale.getDefault()),
                    "phone.error.invalid");
        }

        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())).fullname(request.getFullname())
                .email(request.getEmail()).dateBirth(request.getDateBirth()).phone(request.getPhone()).failedAttempt(0)
                .isDeleted(false).role(Role.USER).userStatus(UserStatus.NOTACTIVATED).build();

        userRepository.save(user);
        verificationTokenService.sendVerificationToken(user, null, TitleToken.CONFIRM_ACCOUNT);
        logger.info("User {} register successfully at : {}", request.getEmail(), Instant.now());
        return MessageDTO.builder().status(HttpStatus.CREATED.value())
                .message(message.getMessage("register.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    @Override
    @Transactional(dontRollbackOn = DataNotFoundException.class)
    public MessageDTO verifyLinkAndOtpConfirmAccount(String token, String otp) {
        User user = verifyLinkOrOtp(token, otp, TitleToken.CONFIRM_ACCOUNT);
        user.setUserStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
        logger.info("User {} verify account success at : {}", user.getEmail(), Instant.now());
        return MessageDTO.builder().status(HttpStatus.OK.value())
                .message(message.getMessage("verify.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    @Override
    public MessageDTO changeEmail(EmailRequestDTO request, Authentication authentication) {
        // check email mới có tồn tại ko
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DataInvalidException("email", message.getMessage("email.invalid", null, Locale.getDefault()),
                    "email.error.invalid");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("",
                message.getMessage("email.notfound", null, Locale.getDefault()), ""));
        verificationTokenService.sendVerificationToken(user, request.getEmail(), TitleToken.CHANGE_EMAIL);
        return MessageDTO.builder().status(HttpStatus.CREATED.value())
                .message(message.getMessage("email.send", null, Locale.getDefault())).timestamp(Instant.now()).build();
    }

    @Override
    @Transactional(dontRollbackOn = DataNotFoundException.class)
    public MessageDTO verifyChangeEmail(String token, String otp, String newEmail) {
        User user = verifyLinkOrOtp(token, otp, TitleToken.CHANGE_EMAIL);
        user.setEmail(newEmail);
        userRepository.save(user);
        user.getUserSession().stream().forEach(userSession -> userSession.setActive(false));
        logger.info("User {} verify change email success at : {}", user.getEmail(), Instant.now());
        return MessageDTO.builder().status(HttpStatus.OK.value())
                .message(message.getMessage("verify.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    @Override
    public MessageDTO resetPassword(EmailRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new DataNotFoundException("email",
                message.getMessage("email.notfound", null, Locale.getDefault()), "email.error.invalid"));
        String newPassword = verificationTokenService.sendVerificationToken(user, null, TitleToken.RESET_PASSWORD);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUserStatus(UserStatus.DISABLE);
        userRepository.save(user);
        logger.info("User {} request reset password at : {}", request.getEmail(), Instant.now());
        return MessageDTO.builder().status(HttpStatus.CREATED.value())
                .message(message.getMessage("email.send", null, Locale.getDefault())).timestamp(Instant.now()).build();
    }

    @Override
    public MessageDTO verifyResetPassword(String token, String otp, ChangePasswordDTO request, String email) {
        User user = verifyLinkOrOtp(token, otp, TitleToken.RESET_PASSWORD);
        checkPassword(request, email);
        user.setUserStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
        logger.info("User {} reset password success at : {}", email, Instant.now());
        return MessageDTO.builder().status(HttpStatus.OK.value())
                .message(message.getMessage("changepassword.success", null, Locale.getDefault()))
                .timestamp(Instant.now()).build();
    }

    @Override
    public MessageDTO changePassword(ChangePasswordDTO request, Authentication authentication) {
        String email = authentication.getName();
        checkPassword(request, email);
        logger.info("User {} change password successfully at : {}", email, Instant.now());
        return MessageDTO.builder().status(HttpStatus.OK.value())
                .message(message.getMessage("changepassword.success", null, Locale.getDefault()))
                .timestamp(Instant.now()).build();
    }

    @Override
    public MessageDTO changePhone(ChangePhoneRequestDTO request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("",
                message.getMessage("email.notfound", null, Locale.getDefault()), ""));
        verificationTokenService.sendVerificationToken(user, "", TitleToken.CHANGE_PHONE);
        logger.info("User {} send request to change phone at : {}", email, Instant.now());
        return MessageDTO.builder().status(HttpStatus.CREATED.value())
                .message(message.getMessage("email.send", null, Locale.getDefault())).timestamp(Instant.now()).build();
    }

    @Override
    @Transactional(dontRollbackOn = DataNotFoundException.class)
    public MessageDTO verifyChangePhone(String token, String otp, String newPhone) {
        User user = verifyLinkOrOtp(token, otp, TitleToken.CHANGE_PHONE);
        user.setPhone(newPhone);
        userRepository.save(user);
        user.getUserSession().stream().forEach(userSession -> userSession.setActive(false));
        logger.info("User {} verify change phone success at : {}", user.getEmail(), Instant.now());
        return MessageDTO.builder().status(HttpStatus.OK.value())
                .message(message.getMessage("verify.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    private User verifyLinkOrOtp(String token, String otp, TitleToken typeEmail) {
        VerificationToken confirmToken = verificationTokenRepository.findByVerifyToken(token, otp, typeEmail)
                .orElseThrow(() -> new DataInvalidException("email", "not found", "changeemail.invalid"));

        if (verificationTokenService.isExpire(token, otp, typeEmail)) {
            logger.warn("Verification token id {} is deleted at {}", confirmToken.getId(), Instant.now());
            verificationTokenRepository.deleteById(confirmToken.getId());
            throw new DataNotFoundException("token", message.getMessage("verify.invalid", null, Locale.getDefault()),
                    "token.invalid");
        }

        User user = userRepository.findByEmail(confirmToken.getUser().getEmail())
                .orElseThrow(() -> new DataNotFoundException("email",
                        message.getMessage("email.notfound", null, Locale.getDefault()), "email.invalid"));
        verificationTokenRepository.deleteById(confirmToken.getId());
        return user;
    }

    private void checkPassword(ChangePasswordDTO request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("email",
                message.getMessage("email.notfound", null, Locale.getDefault()), "email.invalid"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            logger.warn("Old password is not match with user password in database");
            throw new DataInvalidException("email", message.getMessage("password.notmatch", null, Locale.getDefault()),
                    "changeemail.invalid");
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            logger.warn("New password is the same with old password");
            throw new DataInvalidException("email", message.getMessage("password.same", null, Locale.getDefault()),
                    "changeemail.invalid");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            logger.warn("Confirm password is not match with new password");
            throw new DataInvalidException("email",
                    message.getMessage("newpassword.invalid", null, Locale.getDefault()), "changeemail.invalid");
        }

        user.getUserSession().stream().forEach(userSession -> userSession.setActive(false));
        user.setPassword(passwordEncoder.encode(request.getConfirmNewPassword()));
        userRepository.save(user);
    }

}
