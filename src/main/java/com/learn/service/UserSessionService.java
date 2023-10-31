package com.learn.service;

import java.util.List;
import java.util.Optional;

import com.learn.model.UserSession;

public interface UserSessionService {
    
    UserSession save(UserSession userSession);
    
    void save(List<UserSession> userSession);
    
    Optional<UserSession> findBySessionID(String sessionID);
}
