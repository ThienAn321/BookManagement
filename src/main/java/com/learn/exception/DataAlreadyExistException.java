package com.learn.exception;

public class DataAlreadyExistException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public DataAlreadyExistException(String message) {
        super(message);
    }
}
