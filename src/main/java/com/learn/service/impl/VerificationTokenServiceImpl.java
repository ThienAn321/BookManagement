package com.learn.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.model.VerificationToken;
import com.learn.model.User;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.VerificationTokenService;
import com.learn.util.DateUtil;
import com.learn.util.OTPUtil;
import com.learn.service.EmailService;

import jakarta.mail.MessagingException;

@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository confirmTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private OTPUtil otpUtil;

    @Override
    public void sendConfirmationToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpireAt = now.plusMinutes(15);

        String token = UUID.randomUUID().toString();
        String otp = otpUtil.generateOTP();
        VerificationToken confirmationToken = VerificationToken.builder().token(token).otp(otp)
                .createdAt(dateUtil.createDate(now)).expireAt(dateUtil.createDate(tokenExpireAt)).isExpired(false)
                .user(user).build();
        save(confirmationToken);
        try {
            emailService.sendMail("thienan98765123@gmail.com", user.getEmail(), "Confirm Account",
                    emailService.buildEmail(user.getFullname(), token, otp));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VerificationToken save(VerificationToken token) {
        return confirmTokenRepository.save(token);
    }

    @Override
    public Optional<VerificationToken> findByTokenOrOtp(String token, String otp) {
        return confirmTokenRepository.findByTokenOrOtp(token, otp);
    }

    @Override
    public void delete(Integer id) {
        confirmTokenRepository.deleteById(id);
    }

//    @Override
//    public Optional<VerificationToken> findByOtp(String otp) {
//        return confirmTokenRepository.findByOtp(otp);
//    }

}
