package com.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.AuthenticationService;
import com.learn.service.dto.AuthenticationRequest;
import com.learn.service.dto.AuthenticationResponse;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> autheticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    
}
