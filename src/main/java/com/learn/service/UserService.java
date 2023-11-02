package com.learn.service;

import java.util.List;
import java.util.Optional;

import com.learn.model.User;
import com.learn.service.dto.AuthenticationRequest;
import com.learn.service.dto.RegisterDTO;
import com.learn.service.dto.RequestRefreshDTO;

public interface UserService {
    List<User> findAll();

    Optional<User> findByEmail(String email);

    public void register(RegisterDTO request);

    public boolean verifyRegisterAccount(String token, String otp);
    
    public void resendVerify(RequestRefreshDTO request);
    
    public void loginFailed(AuthenticationRequest request, User user);

}
