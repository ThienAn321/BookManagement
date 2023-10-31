package com.learn.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.model.User;
import com.learn.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserAPI {
    
    @Autowired
    private UserService userSerivce;
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userSerivce.findAll();
    }
    
}
