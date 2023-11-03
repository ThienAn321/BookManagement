package com.learn.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ObjectDTO {

    private Integer statusCode;

    private String message;

    private Instant timestamp;

}
