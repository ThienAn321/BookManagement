package com.learn.service.impl;

import java.time.LocalDateTime;
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
import com.learn.util.DateUtil;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private DateUtil dateUtil;

    @Override
    public UserSession save(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

    @Override
    public void saveAll(User user, String jti) {
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime expireTokenDate = createDate.plusMinutes(30);
        LocalDateTime expireRefreshTokenDate = createDate.plusMinutes(60);
        
        UserSession accessToken = UserSession.builder().sessionID(jti).isActive(true).isRefreshToken(false)
                .createAt(dateUtil.createDate(createDate)).expireAt(dateUtil.createDate(expireTokenDate)).user(user)
                .build();
        UserSession refreshToken = UserSession.builder().sessionID(jti).isActive(true).isRefreshToken(true)
                .createAt(dateUtil.createDate(createDate)).expireAt(dateUtil.createDate(expireRefreshTokenDate)).user(user)
                .build();
        
        List<UserSession> userSession = new ArrayList<>(Arrays.asList(accessToken, refreshToken));
        userSessionRepository.saveAll(userSession);
    }

    @Override
    public Optional<UserSession> findBySessionID(String sessionID) {
        return Optional.of(userSessionRepository.findBySessionID(sessionID)).get();
    }

}
