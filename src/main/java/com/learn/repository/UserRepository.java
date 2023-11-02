package com.learn.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    
    @Modifying
    @Query(value = "UPDATE user u SET u.lock_time = ?1 WHERE u.email = ?2", nativeQuery = true)
    void updateLockTime(Instant lockTime, String email);

    @Modifying
    @Query(value = "UPDATE user u SET u.failed_attempt = ?1 WHERE u.email = ?2", nativeQuery = true)
    void updateFailedAttempts(int failAttempts, String email);

}
