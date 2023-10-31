package com.learn.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDTO {

    private Integer statusCode;

    private String message;
    
    private Instant timestamp;
    
}
