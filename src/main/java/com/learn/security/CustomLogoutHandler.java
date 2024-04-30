package com.learn.security;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.learn.exception.DataNotFoundException;
import com.learn.model.UserSession;
import com.learn.repository.UserSessionRepository;
import com.learn.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;

    private final UserSessionRepository userSessionRepository;

    private final MessageSource message;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);
        if (!jwtService.isRefreshToken(jwt)) {
            String jti = jwtService.getId(jwt);
            UserSession userSession = userSessionRepository.findSessionId(jti)
                    .orElseThrow(() -> new DataNotFoundException("refreshtoken",
                            message.getMessage("usersession.notfound", null, Locale.getDefault()),
                            "refreshtoken.error.invalid"));
            userSession.setActive(false);
            userSessionRepository.save(userSession);
        }
    }

}
