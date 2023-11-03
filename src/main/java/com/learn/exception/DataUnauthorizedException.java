package com.learn.exception;

public class DataUnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public DataUnauthorizedException(String message) {
        super(message);
    }
}
