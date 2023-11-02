package com.learn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.model.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    Optional<UserSession> findBySessionID(String sessionID);

    @Query("SELECT u FROM UserSession u WHERE u.sessionID = ?1 AND u.isRefreshToken = TRUE")
    Optional<UserSession> findRefreshToken(String sessionID);

    @Modifying
    @Query("DELETE FROM UserSession u WHERE u.sessionID = ?1 AND u.isRefreshToken = FALSE")
    void deleteToken(String sessionID);
}
