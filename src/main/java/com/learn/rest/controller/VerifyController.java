package com.learn.rest.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;
import com.learn.service.dto.EmailRequestDTO;
import com.learn.service.dto.ObjectDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class VerifyController {

    private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/confirm")
    public ResponseEntity<ObjectDTO> confirmByLink(@RequestParam("value") String value) {
        // value ở đây có thể là link verify hoặc otp
        // verifyRegisterAccount check link verify hoac otp
        logger.info("Token {} verify at {}", value, Instant.now());
        return new ResponseEntity<>(userService.verifyLinkAndOtpConfirmAccount(value, value), HttpStatus.OK);
    }

    @PutMapping("/sendChangeEmail")
    public ResponseEntity<ObjectDTO> resend(@RequestParam("value") String value,
            @RequestBody @Valid EmailRequestDTO request) {
        logger.info("User {} send verify to change email at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(userService.verifyChangeEmail(value, value, request), HttpStatus.OK);
    }

}
