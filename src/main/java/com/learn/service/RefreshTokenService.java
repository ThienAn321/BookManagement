package com.learn.service;

import com.learn.service.dto.response.RefreshResponseDTO;
import com.learn.service.dto.response.RefreshTokenDTO;

public interface RefreshTokenService {
    
    RefreshResponseDTO refreshToken(RefreshTokenDTO request);
    
}
