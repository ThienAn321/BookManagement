package com.learn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.model.UserSession;
import com.learn.repository.UserSessionRepository;
import com.learn.service.UserSessionService;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    @Autowired
    UserSessionRepository userSessionRepository;

    @Override
    public UserSession save(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

    @Override
    public void save(List<UserSession> userSession) {
        userSessionRepository.saveAll(userSession);
    }

    @Override
    public Optional<UserSession> findBySessionID(String sessionID) {
        return Optional.of(userSessionRepository.findBySessionID(sessionID)).get();
    }

}
