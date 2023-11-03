package com.learn.exception;

public class DataInvalidException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public DataInvalidException(String message) {
        super(message);
    }
}
