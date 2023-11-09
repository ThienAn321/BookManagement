package com.learn.service;

import com.learn.model.User;
import com.learn.model.enumeration.TitleToken;

public interface VerificationTokenService {

    void sendVerificationToken(User user, TitleToken typeEmail);
    
    String sendResetPassword(User user);

    boolean isExpire(String token, String otp, TitleToken typeEmail);

}
