package com.learn.service;

import com.learn.service.dto.RefreshResponseDTO;
import com.learn.service.dto.RefreshTokenDTO;

public interface RefreshTokenService {
    
    RefreshResponseDTO refreshToken(RefreshTokenDTO request);
    
}
