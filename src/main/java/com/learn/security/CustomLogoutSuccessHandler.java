package com.learn.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.service.JwtService;
import com.learn.service.dto.response.MessageDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtService jwtService;

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);
        if (jwtService.isRefreshToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            handleInvalidCorrelationId("This is not access token", HttpStatus.UNAUTHORIZED, response);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            handleInvalidCorrelationId("Logout successfully", HttpStatus.OK, response);
            SecurityContextHolder.clearContext();
        }
    }

    private void handleInvalidCorrelationId(String message, HttpStatus status, HttpServletResponse response)
            throws IOException {
        MessageDTO error = new MessageDTO();
        error.setStatus(status.value());
        error.setMessage(message);
        error.setTimestamp(Instant.now());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

}
