package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.exception.DataInvalidException;
import com.learn.model.User;
import com.learn.model.VerificationToken;
import com.learn.model.enumeration.TitleToken;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.EmailService;
import com.learn.service.VerificationTokenService;
import com.learn.util.OTPUtil;
import com.learn.util.PasswordUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final Logger logger = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);

    @Value("${verify.expire}")
    private int verifyExpire;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailService emailService;

    private final OTPUtil otpUtil;

    private final PasswordUtil passwordUtil;

    private final MessageSource message;

    @Override
    public String sendVerificationToken(User user, String newEmail, TitleToken typeEmail) {
        if (verificationTokenRepository.findByUserAndTypeToken(user.getId(), typeEmail).isPresent()) {
            throw new DataInvalidException("", message.getMessage("verify.notexpire", null, Locale.getDefault()),
                    "linkverify.notexpire");
        }
        Instant expireTokenDate = Instant.now().plus(verifyExpire, ChronoUnit.MINUTES);
        String token = UUID.randomUUID().toString();
        String otpGenerate = otpUtil.generateOTP();
        String newPassword = passwordUtil.generatePassword();
        VerificationToken verificationToken = VerificationToken.builder().token(token).otp(otpGenerate)
                .titleToken(typeEmail).expireAt(expireTokenDate).isVerify(false).isExpire(false).user(user).build();
        logger.info("Send link verify for user {} at : {}", user.getEmail(), Instant.now());
        verificationTokenRepository.save(verificationToken);
        try {
            if (typeEmail.equals(TitleToken.CONFIRM_ACCOUNT)) {
                String title = "Confirm account";
                emailService.sendMail("thienan98765123@gmail.com", user.getEmail(), title,
                        emailService.buildEmail(user.getFullname(), token, otpGenerate));
            }
            if (typeEmail.equals(TitleToken.CHANGE_EMAIL)) {
                String title = "Change email";
                emailService.sendMail("thienan98765123@gmail.com", newEmail, title,
                        emailService.buildEmailChangeEmail(user.getFullname(), token, otpGenerate));
            }
            if (typeEmail.equals(TitleToken.RESET_PASSWORD)) {
                String title = "Reset password";
                emailService.sendMail("thienan98765123@gmail.com", user.getEmail(), title,
                        emailService.buildEmailResetPassword(user.getFullname(), token, otpGenerate, newPassword));
            }

        } catch (MessagingException ex) {
            logger.error("Cant send link verify for user {} at : {}", user.getEmail(), Instant.now());
            throw new DataInvalidException("", message.getMessage("email.error", null, Locale.getDefault()),
                    "email.error");
        }
        // return newPassword để set reset_password vào db
        return newPassword;
    }

    @Override
    public boolean isExpire(String token, String otp, TitleToken typeEmail) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByVerifyToken(token, otp,
                typeEmail);
        if (verificationToken.isPresent()) {
            Instant expire = verificationToken.get().getExpireAt();
            if (expire.compareTo(Instant.now()) < 0) {
                verificationToken.get().setExpire(true);
                return true;
            }
        }
        return false;
    }

}
