package com.learn.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.learn.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByTokenOrOtp(String token, String otp);

    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.expireAt <= ?1")
    void deleteAllExpiredSince(Instant instant);

}
