package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.model.VerificationToken;
import com.learn.model.enumeration.TitleToken;
import com.learn.exception.DataInvalidException;
import com.learn.model.User;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.VerificationTokenService;
import com.learn.util.OTPUtil;
import com.learn.util.PasswordUtil;
import com.learn.service.EmailService;

import jakarta.mail.MessagingException;

@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);

    @Value("${verify.expire}")
    private int verifyExpire;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPUtil otpUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    private void sendEmail(User user, TitleToken typeEmail) {
        if (verificationTokenRepository.findByUserAndTypeToken(user.getId(), typeEmail).isPresent()) {
            throw new DataInvalidException("error to send mail");
        }

        Instant expireTokenDate = Instant.now().plus(verifyExpire, ChronoUnit.MINUTES);
        String token = UUID.randomUUID().toString();
        String otpGenerate = otpUtil.generateOTP();
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
                emailService.sendMail("thienan98765123@gmail.com", user.getEmail(), title,
                        emailService.buildEmailChangeEmail(user.getFullname(), token, otpGenerate));
            }

        } catch (MessagingException e) {
            logger.error("Cant send link verify for user {} at : {}", user.getEmail(), Instant.now());
            throw new DataInvalidException("{email.error}");
        }
    }

    // bad code
    private String sendEmailResetPassword(User user) {
        if (verificationTokenRepository.findByUserAndTypeToken(user.getId(), TitleToken.RESET_PASSWORD).isPresent()) {
            throw new DataInvalidException("error to send mail");
        }

        Instant expireTokenDate = Instant.now().plus(verifyExpire, ChronoUnit.MINUTES);
        String token = UUID.randomUUID().toString();
        String otpGenerate = otpUtil.generateOTP();
        String newPassword = passwordUtil.generatePassword();
        VerificationToken verificationToken = VerificationToken.builder().token(token).otp(otpGenerate)
                .titleToken(TitleToken.RESET_PASSWORD).expireAt(expireTokenDate).isVerify(false).isExpire(false)
                .user(user).build();
        logger.info("Send link verify for user {} at : {}", user.getEmail(), Instant.now());
        verificationTokenRepository.save(verificationToken);
        try {
            String title = "Reset password";
            emailService.sendMail("thienan98765123@gmail.com", user.getEmail(), title,
                    emailService.buildEmailResetPassword(user.getFullname(), token, otpGenerate, newPassword));

        } catch (MessagingException e) {
            logger.error("Cant send link verify for user {} at : {}", user.getEmail(), Instant.now());
            throw new DataInvalidException("{email.error}");
        }

        return newPassword;
    }

    @Override
    public void sendVerificationToken(User user, TitleToken typeEmail) {
        sendEmail(user, typeEmail);
    }

    @Override
    public String sendResetPassword(User user) {
        return sendEmailResetPassword(user);
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
