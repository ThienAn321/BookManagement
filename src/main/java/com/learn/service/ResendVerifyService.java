package com.learn.service;

import com.learn.service.dto.request.EmailRequestDTO;
import com.learn.service.dto.response.MessageDTO;

public interface ResendVerifyService {
    
    MessageDTO resendVerifyAccount(EmailRequestDTO request);
    
    MessageDTO resendChangeEmail(EmailRequestDTO request);
    
}
