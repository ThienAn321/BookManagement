package com.learn.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.RefreshTokenService;
import com.learn.service.dto.RefreshTokenDTO;
import com.learn.service.dto.RequestRefreshDTO;
import com.learn.service.dto.ResponseRefreshDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class RefreshToken {

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ResponseRefreshDTO> refresh(@RequestBody @Valid RefreshTokenDTO request) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

}
