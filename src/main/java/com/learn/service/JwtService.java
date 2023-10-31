package com.learn.service;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;


public interface JwtService {
    
    public String getUsernameFromToken(String token);

    public String generateToken(UserDetails userDetails, String id, boolean refreshToken);

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, String id);

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails, String id);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public String getId(String token);

    public boolean isTokenExpired(String jwt);
    
    public Date getExpirationToken(String token);
    
    public Date getIssuedAt(String token);
}
