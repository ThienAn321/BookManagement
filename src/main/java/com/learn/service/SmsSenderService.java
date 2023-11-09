package com.learn.service;

import com.learn.service.dto.ChangephoneRequestDTO;

public interface SmsSenderService {

    String sendSMS(ChangephoneRequestDTO request);
    
}
