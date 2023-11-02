package com.learn.exception;

public class UserSessionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserSessionNotFoundException(String message) {
        super(message);
    }

}
