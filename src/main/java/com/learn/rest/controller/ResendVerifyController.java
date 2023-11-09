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

import com.learn.service.ResendVerifyService;
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.EmailRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class ResendVerifyController {

    private static Logger logger = LoggerFactory.getLogger(ResendVerifyController.class);

    @Autowired
    private ResendVerifyService resendVerifyService;

    @PostMapping("/resendConfirmAccount")
    public ResponseEntity<ObjectDTO> resendConfirmAccount(@RequestBody @Valid EmailRequestDTO request) {
        logger.info("Resend verify user {} at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(resendVerifyService.resendVerifyAccount(request), HttpStatus.OK);
    }
    
    @PostMapping("/resendChangeEmail")
    public ResponseEntity<ObjectDTO> resendChangeEmail(@RequestBody @Valid EmailRequestDTO request) {
        logger.info("Resend verify user {} at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(resendVerifyService.resendChangeEmail(request), HttpStatus.OK);
    }

}
