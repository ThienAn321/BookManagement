package com.learn.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendMail(String from, String to, String subject, String message) throws MessagingException;

    String buildEmail(String name, String link, String otp);
    
    String buildEmailChangeEmail(String name, String link, String otp);
    
    String buildEmailResetPassword(String name, String link, String otp, String password);

}
