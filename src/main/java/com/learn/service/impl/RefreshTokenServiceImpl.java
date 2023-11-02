package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.exception.LinkVerifyIsNotExpired;
import com.learn.exception.UserSessionNotFoundException;
import com.learn.model.UserSession;
import com.learn.service.JwtService;
import com.learn.service.RefreshTokenService;
import com.learn.service.UserSessionService;
import com.learn.service.dto.RefreshTokenDTO;
import com.learn.service.dto.ResponseRefreshDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private JwtService jwtService;

    @Override
    public ResponseRefreshDTO refreshToken(RefreshTokenDTO request) {
        String jti = jwtService.getId(request.getRefreshToken());
        UserSession userSession = userSessionService.findRefreshToken(jti)
                .orElseThrow(() -> new UserSessionNotFoundException("Session không tìm thấy"));
        CustomUserDetails userDetails = new CustomUserDetails(userSession.getUser());
        String newToken = jwtService.generateToken(userDetails, jti, false);
        if (userSession.getExpireAt().compareTo(Instant.now()) > 0) {
            userSessionService.deleteToken(jti);
            UserSession accessToken = UserSession.builder().sessionID(jti).isActive(true).isRefreshToken(false)
                    .expireAt(Instant.now().plus(30, ChronoUnit.MINUTES)).user(userSession.getUser()).build();
            userSessionService.save(accessToken);
        } else {
            throw new LinkVerifyIsNotExpired("Refresh token expired ! Vui lòng đăng nhập lại ");
        }
        return ResponseRefreshDTO.builder().token(newToken).build();

    }

}
