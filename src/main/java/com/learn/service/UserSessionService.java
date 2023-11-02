package com.learn.service;

import java.util.Optional;

import com.learn.model.User;
import com.learn.model.UserSession;

public interface UserSessionService {
    
    UserSession save(UserSession userSession);
    
    void saveAll(User user, String jti);
    
    Optional<UserSession> findBySessionID(String sessionID);
    
    Optional<UserSession> findRefreshToken(String sessionID);
    
    void deleteToken(String sessionID);
}
