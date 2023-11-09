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

import com.learn.service.AuthenticationService;
import com.learn.service.dto.AuthenticationRequestDTO;
import com.learn.service.dto.AuthenticationResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO request) {
        logger.info("User {} try to login at : {}", request.getEmail(), Instant.now());
        return ResponseEntity.ok(authenticationService.login(request));
    }

}
