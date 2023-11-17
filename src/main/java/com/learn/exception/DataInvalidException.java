package com.learn.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String field;
    
    private final String messageCode;

    public DataInvalidException(String field, String message, String messageCode) {
        super(message);
        this.field = field;
        this.messageCode = messageCode;
    }
}
