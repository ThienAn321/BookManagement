package com.learn.service.impl;

import java.time.Instant;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MessageSource message;

    @Override
    public RefreshResponseDTO refreshToken(RefreshTokenDTO request) {
        if (!jwtService.isRefreshToken(request.getRefreshToken())) {
            logger.error("Not a refresh token at : {}", Instant.now());
            throw new DataInvalidException(message.getMessage("refresh.invalid", null, Locale.getDefault()));
        }

        String jti = jwtService.getId(request.getRefreshToken());
        UserSession userSession = userSessionRepository.findSessionId(jti).orElseThrow(
                () -> new DataNotFoundException(message.getMessage("usersession.notfound", null, Locale.getDefault())));
        if (userSession.getExpireAt().compareTo(Instant.now()) > 0 && userSession.isActive()) {
            CustomUserDetails userDetails = new CustomUserDetails(userSession.getUser());
            String newToken = jwtService.generateToken(userDetails, jti, false);
            logger.info("User {} generate new access token successfully at : {}", userDetails.getUsername(),
                    Instant.now());
            return RefreshResponseDTO.builder().token(newToken).build();
        }

        logger.error("Refresh token {} is expired", request.getRefreshToken());
        throw new DataInvalidException(message.getMessage("refreshtoken.expired", null, Locale.getDefault()));
    }

}
