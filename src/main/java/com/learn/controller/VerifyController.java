package com.learn.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;

@RestController
@RequestMapping("api/v1/auth")
public class VerifyController {

    @Autowired
    private UserService userService;

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmByLink(@RequestParam("value") String value) {
        Map<String, Object> map = new LinkedHashMap<>();
        // value ở đây có thể là link verify hoặc otp
        // verifyRegisterAccount check link verify hoac otp
        userService.verifyRegisterAccount(value, value);
        map.put("status", HttpStatus.OK.value());
        map.put("message", "Verify thành công !");
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

}
