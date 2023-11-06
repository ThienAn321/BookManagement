package com.learn.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.ResendVerifyService;
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RefreshRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class ResendVerifyController {

    @Autowired
    private ResendVerifyService resendVerifyService;

    @PostMapping("/resend")
    public ResponseEntity<ObjectDTO> resend(@RequestBody @Valid RefreshRequestDTO request) {
        return new ResponseEntity<>(resendVerifyService.resendVerify(request), HttpStatus.OK);
    }

}
