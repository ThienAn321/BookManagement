package com.learn.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Page<User> findAll(Specification<User> specification, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String name);

    Optional<User> findByPhone(String phone);

    @Modifying
    @Query("UPDATE User u SET u.lockTime = ?1 WHERE u.email = ?2")
    void updateLockTime(Instant lockTime, String email);

    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    void updateFailedAttempts(int failAttempts, String email);

}
