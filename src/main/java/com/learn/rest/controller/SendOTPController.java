package com.learn.rest.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;
import com.learn.service.dto.ChangephoneRequestDTO;
import com.learn.service.dto.ObjectDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class SendOTPController {
    
    private static final Logger logger = LoggerFactory.getLogger(SendOTPController.class);
    
    @Autowired
    private UserService userService;

    @PostMapping("/changephone")
    public ResponseEntity<ObjectDTO> resend(@RequestBody @Valid ChangephoneRequestDTO request) {
        logger.info("User {} send verify to change email at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(userService.changePhone(request), HttpStatus.OK);
    }

}