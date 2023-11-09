package com.learn.rest.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;
import com.learn.service.dto.ChangePasswordDTO;
import com.learn.service.dto.EmailRequestDTO;
import com.learn.service.dto.ObjectDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class ResetPasswordController {

    private static Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/resetpassword")
    public ResponseEntity<ObjectDTO> resend(@RequestBody @Valid EmailRequestDTO request) {
        logger.info("User {} send verify to change email at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(userService.resetPassword(request), HttpStatus.OK);
    }

    @PutMapping("/resetpassword")
    public ResponseEntity<ObjectDTO> register(@RequestParam("value") String value,
            @RequestBody @Valid ChangePasswordDTO request, @RequestHeader(value = "email") String email) {
        logger.info("User {} reset password at : {}", email, Instant.now());
        return new ResponseEntity<>(userService.verifyResetPassword(value, value, request, email), HttpStatus.OK);
    }

}
