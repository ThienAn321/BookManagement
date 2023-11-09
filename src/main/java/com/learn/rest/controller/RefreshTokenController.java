package com.learn.rest.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.RefreshTokenService;
import com.learn.service.dto.RefreshResponseDTO;
import com.learn.service.dto.RefreshTokenDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class RefreshTokenController {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenController.class);

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refresh(@RequestBody @Valid RefreshTokenDTO request) {
        logger.info("Refresh token is call at : {}", Instant.now());
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

}
