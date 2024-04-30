package com.learn.service.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorDTO {
    
    private String fieldName;

    private String message;

    private String messageCode;

    private Instant timestamp;
    
}
