package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.learn.exception.DataNotFoundException;
import com.learn.model.User;
import com.learn.model.UserSession;
import com.learn.repository.UserRepository;
import com.learn.repository.UserSessionRepository;
import com.learn.service.AuthenticationService;
import com.learn.service.JwtService;
import com.learn.service.UserService;
import com.learn.service.dto.AuthenticationRequestDTO;
import com.learn.service.dto.AuthenticationResponseDTO;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Email not found"));

        // loginFailed
        userService.loginFailed(request, user);

        // handle exception o day
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Instant expireDate = Instant.now().plus(60, ChronoUnit.MINUTES);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateToken(userDetails, null, false);
        String jti = jwtService.getId(accessToken);
        String refreshToken = jwtService.generateToken(userDetails, jti, true);
        UserSession userSession = UserSession.builder().sessionID(jti).isActive(true).expireAt(null)
                .expireAt(expireDate).user(user).build();
        userSessionRepository.save(userSession);
        return AuthenticationResponseDTO.builder().token(accessToken).refreshToken(refreshToken).build();
    }

}
