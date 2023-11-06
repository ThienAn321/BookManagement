package com.learn.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;
import com.learn.service.dto.ObjectDTO;

@RestController
@RequestMapping("api/v1/auth")
public class VerifyController {

    @Autowired
    private UserService userService;

    @GetMapping("/confirm")
    public ResponseEntity<ObjectDTO> confirmByLink(@RequestParam("value") String value) {
        // value ở đây có thể là link verify hoặc otp
        // verifyRegisterAccount check link verify hoac otp
        return new ResponseEntity<>(userService.verifyRegisterAccount(value, value), HttpStatus.OK);
    }

}
