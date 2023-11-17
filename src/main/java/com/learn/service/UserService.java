package com.learn.service;

import org.springframework.security.core.Authentication;

import com.learn.service.dto.UserDTO;
import com.learn.service.dto.request.ChangePasswordDTO;
import com.learn.service.dto.request.ChangePhoneRequestDTO;
import com.learn.service.dto.request.EmailRequestDTO;
import com.learn.service.dto.response.MessageDTO;

public interface UserService {

    MessageDTO register(UserDTO request);

    MessageDTO verifyLinkAndOtpConfirmAccount(String token, String otp);

    MessageDTO changeEmail(EmailRequestDTO request, Authentication authentication);

    MessageDTO verifyChangeEmail(String token, String otp, String newEmail);

    MessageDTO resetPassword(EmailRequestDTO request);

    MessageDTO verifyResetPassword(String token, String otp, ChangePasswordDTO request, String email);

    MessageDTO changePassword(ChangePasswordDTO request, Authentication authentication);

    MessageDTO changePhone(ChangePhoneRequestDTO request, Authentication authentication);

    MessageDTO verifyChangePhone(String token, String otp, String newPhone);

}
