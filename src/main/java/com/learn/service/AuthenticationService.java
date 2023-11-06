package com.learn.service;

import com.learn.service.dto.AuthenticationRequestDTO;
import com.learn.service.dto.AuthenticationResponseDTO;

public interface AuthenticationService {

    AuthenticationResponseDTO login(AuthenticationRequestDTO request);

}
