package com.learn.controller;

import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;
import com.learn.service.dto.RegisterDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO request) {
        userService.register(request);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", HttpStatus.CREATED.value());
        map.put("message", "Đăng ký thành công !");
        map.put("verify", "Vui lòng kiểm tra gmail");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
    
}
