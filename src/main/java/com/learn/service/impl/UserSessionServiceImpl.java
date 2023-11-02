package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.model.User;
import com.learn.model.UserSession;
import com.learn.repository.UserSessionRepository;
import com.learn.service.UserSessionService;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public UserSession save(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

    @Override
    public void saveAll(User user, String jti) {
        Instant expireTokenDate = Instant.now().plus(30, ChronoUnit.MINUTES);
        Instant expireRefreshTokenDate = Instant.now().plus(60, ChronoUnit.MINUTES);

        UserSession accessToken = UserSession.builder().sessionID(jti).isActive(true).isRefreshToken(false)
                .expireAt(expireTokenDate).user(user).build();
        UserSession refreshToken = UserSession.builder().sessionID(jti).isActive(true).isRefreshToken(true)
                .expireAt(expireRefreshTokenDate).user(user).build();

        List<UserSession> userSession = new ArrayList<>(Arrays.asList(accessToken, refreshToken));
        userSessionRepository.saveAll(userSession);
    }

    @Override
    public Optional<UserSession> findBySessionID(String sessionID) {
        return userSessionRepository.findBySessionID(sessionID);
    }

    @Override
    public Optional<UserSession> findRefreshToken(String sessionID) {
        return userSessionRepository.findRefreshToken(sessionID);
    }

    @Override
    public void deleteToken(String sessionID) {
        userSessionRepository.deleteToken(sessionID);
    }

}
