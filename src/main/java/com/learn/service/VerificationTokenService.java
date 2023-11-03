package com.learn.service;

import com.learn.model.User;

public interface VerificationTokenService {

    void sendVerificationToken(User user);

    boolean isExpire(String token, String otp);

}
