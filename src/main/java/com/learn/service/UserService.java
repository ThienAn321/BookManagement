package com.learn.service;

import org.springframework.security.core.Authentication;

import com.learn.service.dto.ChangePasswordDTO;
import com.learn.service.dto.ChangephoneRequestDTO;
import com.learn.service.dto.EmailRequestDTO;
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RegisterDTO;

public interface UserService {

    ObjectDTO register(RegisterDTO request);

    ObjectDTO verifyLinkAndOtpConfirmAccount(String token, String otp);

    ObjectDTO changeEmail(EmailRequestDTO request);

    ObjectDTO verifyChangeEmail(String token, String otp, EmailRequestDTO request);

    ObjectDTO resetPassword(EmailRequestDTO request);
    
    ObjectDTO verifyResetPassword(String token, String otp, ChangePasswordDTO request, String email);

    ObjectDTO changePassword(ChangePasswordDTO request, Authentication authentication);
    
    ObjectDTO changePhone(ChangephoneRequestDTO request);

}
