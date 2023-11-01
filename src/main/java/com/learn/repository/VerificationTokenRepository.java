package com.learn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByTokenOrOtp(String token, String otp);
    
//    Optional<VerificationToken> findByOtp(String otp);
}
