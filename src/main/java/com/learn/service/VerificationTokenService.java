package com.learn.service;

import com.learn.model.User;
import com.learn.model.enumeration.TitleToken;

public interface VerificationTokenService {

    String sendVerificationToken(User user, String newEmail, TitleToken typeEmail);

    boolean isExpire(String token, String otp, TitleToken typeEmail);

}
