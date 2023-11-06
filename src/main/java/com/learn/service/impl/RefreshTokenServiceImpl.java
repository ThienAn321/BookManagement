package com.learn.service.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.exception.DataInvalidException;
import com.learn.exception.DataNotFoundException;
import com.learn.model.UserSession;
import com.learn.repository.UserSessionRepository;
import com.learn.service.JwtService;
import com.learn.service.RefreshTokenService;
import com.learn.service.dto.RefreshResponseDTO;
import com.learn.service.dto.RefreshTokenDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public RefreshResponseDTO refreshToken(RefreshTokenDTO request) {
        if (!jwtService.isRefreshToken(request.getRefreshToken())) {
            throw new DataInvalidException("Not refresh token");
        }
        String jti = jwtService.getId(request.getRefreshToken());
        UserSession userSession = userSessionRepository.findRefreshToken(jti)
                .orElseThrow(() -> new DataNotFoundException("{usersession.notfound}"));
        if (userSession.getExpireAt().compareTo(Instant.now()) > 0) {
            CustomUserDetails userDetails = new CustomUserDetails(userSession.getUser());
            String newToken = jwtService.generateToken(userDetails, jti, false);
            return RefreshResponseDTO.builder().token(newToken).build();
        }
        throw new DataInvalidException("{refreshtoken.expired}");
    }

}
