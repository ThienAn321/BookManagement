package com.learn.service;

import com.learn.service.dto.request.AuthenticationRequestDTO;
import com.learn.service.dto.response.AuthenticationResponseDTO;

public interface AuthenticationService {

    AuthenticationResponseDTO login(AuthenticationRequestDTO request);

}
