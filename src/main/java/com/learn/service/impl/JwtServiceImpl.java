package com.learn.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.learn.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret_key;

    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

    @Value("${jwt.refreshExpiration}")
    private Integer refreshExpirationDate;

    @Override
    public String generateToken(UserDetails userDetails, String id, boolean refreshToken) {
        if (refreshToken) {
            return generateRefreshToken(new HashMap<>(), userDetails, id);
        }
        return generateToken(new HashMap<>(), userDetails, id);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, String id) {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        return Jwts.builder().setClaims(extraClaims).setId(id).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails, String id) {
        return Jwts.builder().setClaims(extraClaims).setId(id).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDate))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    private Claims parser(String token)  {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

    }
    
    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = parser(token);
        return claims.getSubject();

    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    @Override
    public boolean isTokenExpired(String token) {
        return getExpirationToken(token).before(new Date());
    }
    
    @Override
    public Date getExpirationToken(String token) {
        Claims claims = parser(token);
        return claims.getExpiration();
    }
    
    @Override
    public Date getIssuedAt(String token) {
        Claims claims = parser(token);
        return claims.getIssuedAt();
    }

    @Override
    public String getId(String token) {
        Claims claims = parser(token);
        return claims.getId();
    }

//
//    @Override
//    public String getId(String token) {
//        return extractClaim(token, Claims::getId);
//    }

}
