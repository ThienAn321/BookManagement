package com.learn.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.model.User;
import com.learn.model.enumeration.Role;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.service.UserService;
import com.learn.service.dto.RegisterDTO;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static String createDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = currentTime.format(formatter);
        return formatDateTime;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.of(userRepository.findByUsername(username)).get();
    }

    @Override
    public void register(RegisterDTO request) {
        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())).fullname(request.getFullname())
                .email(request.getEmail()).phone(request.getPhone()).createAt(createDate()).role(Role.USER)
                .userStatus(UserStatus.ACTIVATED).build();
        userRepository.save(user);
    }

}
