package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.model.VerificationToken;
import com.learn.exception.DataInvalidException;
import com.learn.model.User;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.VerificationTokenService;
import com.learn.util.OTPUtil;
import com.learn.service.EmailService;

import jakarta.mail.MessagingException;

@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPUtil otpUtil;

    @Override
    public void sendVerificationToken(User user) {
        Instant expireTokenDate = Instant.now().plus(15, ChronoUnit.MINUTES);

        String token = UUID.randomUUID().toString();
        String otpGenerate = otpUtil.generateOTP();
        VerificationToken verificationToken = VerificationToken.builder().token(token).otp(otpGenerate)
                .expireAt(expireTokenDate).isVerify(false).isExpire(false).user(user).build();
        verificationTokenRepository.save(verificationToken);
        try {
            emailService.sendMail("thienan98765123@gmail.com", user.getEmail(), "Confirm Account",
                    emailService.buildEmail(user.getFullname(), token, otpGenerate));
        } catch (MessagingException e) {
            throw new DataInvalidException("{email.error}");
        }
    }

    @Override
    public boolean isExpire(String token, String otp) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByTokenOrOtp(token, otp);
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
