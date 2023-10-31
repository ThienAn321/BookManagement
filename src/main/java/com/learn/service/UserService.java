package com.learn.service;

import java.util.List;
import java.util.Optional;

import com.learn.model.User;
import com.learn.service.dto.RegisterDTO;

public interface UserService {
    List<User> findAll();
    
    Optional<User> findByUsername(String username);
    
    public void register(RegisterDTO request);
}
