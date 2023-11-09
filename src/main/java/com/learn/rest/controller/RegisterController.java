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
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RegisterDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ObjectDTO> register(@RequestBody @Valid RegisterDTO request) {
        logger.info("Register use {} at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

}
