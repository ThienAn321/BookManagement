package com.learn.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.model.VerificationToken;
import com.learn.model.enumeration.TitleToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    
    @Query("SELECT t FROM VerificationToken t WHERE t.token = ?1 or t.otp = ?2 and t.titleToken = ?3")
    Optional<VerificationToken> findByVerifyToken(String token, String otp, TitleToken typeToken);
    
    @Query("SELECT t FROM VerificationToken t WHERE t.user.id = ?1 and t.titleToken = ?2")
    Optional<VerificationToken> findByUserAndTypeToken(int id, TitleToken typeToken);

    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.expireAt <= ?1")
    void deleteAllExpiredSince(Instant instant);

}
