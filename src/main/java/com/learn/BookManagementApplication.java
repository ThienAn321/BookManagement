package com.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.learn.config.TwilioConfig;
import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class BookManagementApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(BookManagementApplication.class);
    
    @Autowired
    private TwilioConfig twilioConfig;
    
    @PostConstruct
    public void init() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        logger.info("Twilio initialized with account sid : {}", twilioConfig.getAccountSid());
    }

    public static void main(String[] args) {
        SpringApplication.run(BookManagementApplication.class, args);
    }

}
