package com.learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.learn.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendMail(String from, String to, String subject, String message) throws MessagingException {
        MimeMessage mineMessage = emailSender.createMimeMessage();
        mineMessage.setFrom(new InternetAddress(from));
        mineMessage.setRecipients(MimeMessage.RecipientType.TO, to);
        mineMessage.setSubject(subject);
        mineMessage.setContent(message, "text/html; charset=utf-8");
        emailSender.send(mineMessage);
    }

    @Override
    public String buildEmail(String name, String token, String otp) {
        return "Chào bạn " + name + ",<br>\r\n"
                + "Cảm ơn bạn đã đăng kí tài khoản. Vui lòng nhập OTP hoặc click vào đường link bên dưới để kích hoạt tài khoản :<br>\r\n"
                + "OTP của bạn là : " + otp + "<br>\r\n"
                + "Link : <a href=\"http://localhost:8080/api/v1/auth/confirm?value=" + token + "\">Active account</a><br>\r\n"
                + "OTP và đường link sẽ hết hạn trong vòng 15 phút.<br>\r\n" + "Cảm ơn bạn.";
    }

}
