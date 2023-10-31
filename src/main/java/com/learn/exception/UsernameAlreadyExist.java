package com.learn.exception;

public class UsernameAlreadyExist extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsernameAlreadyExist(String message) {
        super(message);
    }

}
