package com.learn.exception;

public class LinkVerifyNotFound extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public LinkVerifyNotFound(String message) {
        super(message);
    }
}
