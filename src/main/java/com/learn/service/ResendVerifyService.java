package com.learn.service;

import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RefreshRequestDTO;

public interface ResendVerifyService {
    
    ObjectDTO resendVerify(RefreshRequestDTO request);
    
}
