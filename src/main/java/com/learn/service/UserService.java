package com.learn.service;

import com.learn.model.User;
import com.learn.service.dto.AuthenticationRequestDTO;
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RegisterDTO;

public interface UserService {

    ObjectDTO register(RegisterDTO request);

    ObjectDTO verifyRegisterAccount(String token, String otp);

    void loginFailed(AuthenticationRequestDTO request, User user);

}
