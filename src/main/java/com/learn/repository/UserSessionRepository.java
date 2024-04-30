package com.learn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.model.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {

    @Query("SELECT u FROM UserSession u WHERE u.sessionID = ?1")
    Optional<UserSession> findSessionId(String sessionID);



}
