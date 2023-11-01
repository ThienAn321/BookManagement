package com.learn.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    
    public void sendMail(String from, String to, String subject, String message) throws MessagingException;

    public String buildEmail(String name, String link, String otp);
}
