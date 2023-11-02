package com.learn.service;

import java.time.Instant;
import java.util.Optional;

import com.learn.model.VerificationToken;
import com.learn.model.User;

public interface VerificationTokenService {
    public void sendConfirmationToken(User user);

    VerificationToken save(VerificationToken token);

    Optional<VerificationToken> findByTokenOrOtp(String token, String otp);

    void delete(Integer id);
    
    void deleteAllExpiredSince(Instant instant);
    
    void deleteAllVerifyToken();
    
    boolean isExpire(String token, String otp);
    
}
