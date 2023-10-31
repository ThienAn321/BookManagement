package com.learn.controller;

import java.util.Map;
import java.util.Optional;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.exception.UsernameAlreadyExist;
import com.learn.model.User;
import com.learn.service.UserService;
import com.learn.service.dto.RegisterDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO request, BindingResult bindingResult) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            map.put("status", HttpStatus.BAD_REQUEST.value());
            map.put("message", "Đăng ký chưa thành công !");
            bindingResult.getFieldErrors().forEach(error -> map.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userService.findByUsername(request.getUsername());
        if (user.isPresent()) {
            throw new UsernameAlreadyExist("Username đã tồn tại trong database !");
        }

        userService.register(request);
        map.put("status", HttpStatus.CREATED.value());
        map.put("message", "Đăng ký thành công !");
        return new ResponseEntity<>(map, HttpStatus.CREATED);

    }

}
