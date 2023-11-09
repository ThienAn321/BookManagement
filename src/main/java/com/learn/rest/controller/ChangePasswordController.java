package com.learn.rest.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;
import com.learn.service.dto.ChangePasswordDTO;
import com.learn.service.dto.ObjectDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class ChangePasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordController.class);

    @Autowired
    private UserService userService;

    @PutMapping("/changepassword")
    public ResponseEntity<ObjectDTO> register(@RequestBody @Valid ChangePasswordDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User {} change password at : {}", authentication.getPrincipal(), Instant.now());
        return new ResponseEntity<>(userService.changePassword(request, authentication), HttpStatus.OK);
    }

}
