package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.model.VerificationToken;
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
    public void sendConfirmationToken(User user) {
        Instant expireTokenDate = Instant.now().plus(15, ChronoUnit.MINUTES);

        String token = UUID.randomUUID().toString();
        String otp = otpUtil.generateOTP();
        VerificationToken verificationToken = VerificationToken.builder().token(token).otp(otp)
                .expireAt(expireTokenDate).isVerify(false).isExpire(false).user(user).build();
        save(verificationToken);
        try {
            emailService.sendMail("thienan98765123@gmail.com", user.getEmail(), "Confirm Account",
                    emailService.buildEmail(user.getFullname(), token, otp));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VerificationToken save(VerificationToken token) {
        return verificationTokenRepository.save(token);
    }

    @Override
    public Optional<VerificationToken> findByTokenOrOtp(String token, String otp) {
        return verificationTokenRepository.findByTokenOrOtp(token, otp);
    }

    @Override
    public void delete(Integer id) {
        verificationTokenRepository.deleteById(id);
    }

    @Override
    public void deleteAllExpiredSince(Instant instant) {
        verificationTokenRepository.deleteAllExpiredSince(instant);
    }

    @Override
    public void deleteAllVerifyToken() {
        verificationTokenRepository.deleteAllVerifyToken();
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
