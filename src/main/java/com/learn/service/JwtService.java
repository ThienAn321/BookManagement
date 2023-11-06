package com.learn.service;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String getUsernameFromToken(String token);

    String generateToken(UserDetails userDetails, String id, boolean refreshToken);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, String id);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails, String id);

    boolean isTokenValid(String token, UserDetails userDetails);

    String getId(String token);

    boolean isTokenExpired(String jwt);

    Date getExpirationToken(String token);

    Date getIssuedAt(String token);
    
    boolean isRefreshToken(String token);

}
