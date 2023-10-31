package com.learn.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.learn.exception.UserNotFoundException;
import com.learn.model.User;
import com.learn.model.UserSession;
import com.learn.service.AuthenticationService;
import com.learn.service.JwtService;
import com.learn.service.UserService;
import com.learn.service.UserSessionService;
import com.learn.service.dto.AuthenticationRequest;
import com.learn.service.dto.AuthenticationResponse;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private CustomUserDetails userDetails;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public static String createDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentTime.format(formatter);

    }

    public static String expireDate() {
        LocalDateTime currentTime = LocalDateTime.now().plusMinutes(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentTime.format(formatter);
    }
    
    public static String expireRefreshTokenDate() {
        LocalDateTime currentTime = LocalDateTime.now().plusMinutes(60);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentTime.format(formatter);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Username not found"));
        userDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateToken(userDetails, null, false); // Token
        String jti = jwtService.getId(accessToken);
        String refreshToken = jwtService.generateToken(userDetails, jti, true); // refreshToken
        UserSession access = UserSession.builder().sessionID(jti).isActive(true).isRefreshToken(false)
                .createAt(createDate()).expireAt(expireDate()).user(user).build();
        UserSession refresh = UserSession.builder().sessionID(jti).isActive(true).isRefreshToken(true)
                .createAt(createDate()).expireAt(expireRefreshTokenDate()).user(user).build();
        ArrayList<UserSession> arrayList = new ArrayList<>(Arrays.asList(access, refresh));
        userSessionService.save(arrayList);
        return AuthenticationResponse.builder().token(accessToken).refreshToken(refreshToken).build();
    }

}
