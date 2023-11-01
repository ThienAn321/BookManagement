package com.learn.exception;

public class EmailAlreadyExist extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyExist(String message) {
        super(message);
    }

}
