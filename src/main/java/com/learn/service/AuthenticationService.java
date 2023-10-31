package com.learn.service;

import com.learn.service.dto.AuthenticationRequest;
import com.learn.service.dto.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
