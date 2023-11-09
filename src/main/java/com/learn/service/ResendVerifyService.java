package com.learn.service;

import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.EmailRequestDTO;

public interface ResendVerifyService {
    
    ObjectDTO resendVerifyAccount(EmailRequestDTO request);
    
    ObjectDTO resendChangeEmail(EmailRequestDTO request);
    
}
