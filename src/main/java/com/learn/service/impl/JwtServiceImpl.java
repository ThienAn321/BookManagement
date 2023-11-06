package com.learn.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.learn.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Value("${jwt.secret}")
    private String secret_key;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    @Value("${jwt.refreshExpiration}")
    private int refreshExpirationDate;

    @Override
    public String generateToken(UserDetails userDetails, String id, boolean refreshToken) {
        if (refreshToken) {
            Map<String, Object> map = new HashMap<>();
            map.put("isRefreshToken", true);
            return generateToken(map, userDetails, id);
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

    private Claims parser(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = parser(token);
        return claims.getSubject();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername())) && !isRefreshToken(token) && !isTokenExpired(token);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
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

    @Override
    public boolean isRefreshToken(String token) {
        Claims claims = parser(token);
        return claims.get("isRefreshToken") != null;
    }

}
