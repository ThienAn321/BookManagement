package com.learn.rest.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserService;
import com.learn.service.dto.RequestRefreshDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class ResendVerifyController {

    @Autowired
    UserService userService;

    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody @Valid RequestRefreshDTO request) {
        userService.resendVerify(request);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", HttpStatus.OK.value());
        map.put("verify", "Vui lòng kiểm tra gmail");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
