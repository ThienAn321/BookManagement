package com.learn.exception;

public class EmailNotActived extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailNotActived(String message) {
        super(message);
    }

}
