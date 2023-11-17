package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.learn.exception.DataInvalidException;
import com.learn.exception.DataNotFoundException;
import com.learn.model.UserSession;
import com.learn.repository.UserSessionRepository;
import com.learn.service.JwtService;
import com.learn.service.RefreshTokenService;
import com.learn.service.dto.response.RefreshResponseDTO;
import com.learn.service.dto.response.RefreshTokenDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    private final UserSessionRepository userSessionRepository;

    private final JwtService jwtService;

    private final MessageSource message;

    @Override
    public RefreshResponseDTO refreshToken(RefreshTokenDTO request) {
        if (!jwtService.isRefreshToken(request.getRefreshToken())) {
            logger.error("Not a refresh token at : {}", Instant.now());
            throw new DataInvalidException("refreshtoken",
                    message.getMessage("refresh.invalid", null, Locale.getDefault()), "refreshtoken.error.invalid");
        }

        String jti = jwtService.getId(request.getRefreshToken());
        UserSession userSession = userSessionRepository.findSessionId(jti)
                .orElseThrow(() -> new DataNotFoundException("refreshtoken",
                        message.getMessage("usersession.notfound", null, Locale.getDefault()),
                        "refreshtoken.error.invalid"));
        if (userSession.getExpireAt().compareTo(Instant.now()) > 0 && userSession.isActive()) {
            userSession.setExpireAt(Instant.now().plus(60, ChronoUnit.MINUTES));
            CustomUserDetails userDetails = new CustomUserDetails(userSession.getUser());
            String newToken = jwtService.generateToken(userDetails, jti, false);
            logger.info("User {} generate new access token successfully at : {}", userDetails.getUsername(),
                    Instant.now());
            return RefreshResponseDTO.builder().token(newToken).build();
        }

        logger.error("Refresh token {} is expired", request.getRefreshToken());
        throw new DataInvalidException("refreshtoken", message.getMessage("refreshtoken.expired", null, Locale.getDefault()), "refreshtoken.expired");
    }

}
