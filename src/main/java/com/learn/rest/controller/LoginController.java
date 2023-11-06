package com.learn.rest.controller;

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

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

}
