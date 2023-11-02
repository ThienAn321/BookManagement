package com.learn.exception;

public class EmailAlreadyActived extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyActived(String message) {
        super(message);
    }

}
