package com.learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.learn.exception.EmailNotFoundException;
import com.learn.model.User;
import com.learn.service.AuthenticationService;
import com.learn.service.JwtService;
import com.learn.service.UserService;
import com.learn.service.UserSessionService;
import com.learn.service.dto.AuthenticationRequest;
import com.learn.service.dto.AuthenticationResponse;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

        // loginFailed
        userService.loginFailed(request, user);

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateToken(userDetails, null, false);
        String jti = jwtService.getId(accessToken);
        String refreshToken = jwtService.generateToken(userDetails, jti, true);
        userSessionService.saveAll(user, jti);
        return AuthenticationResponse.builder().token(accessToken).refreshToken(refreshToken).build();
    }

}
