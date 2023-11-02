package com.learn.exception;

public class LinkVerifyIsNotExpired extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LinkVerifyIsNotExpired(String message) {
        super(message);
    }
}
