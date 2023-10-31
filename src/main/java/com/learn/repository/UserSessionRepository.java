package com.learn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.model.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    Optional<UserSession> findBySessionID(String sessionID);
}
