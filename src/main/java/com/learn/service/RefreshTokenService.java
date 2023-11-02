package com.learn.service;

import com.learn.service.dto.RefreshTokenDTO;
import com.learn.service.dto.ResponseRefreshDTO;

public interface RefreshTokenService {
    public ResponseRefreshDTO refreshToken(RefreshTokenDTO request);
}
