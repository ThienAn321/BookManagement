package com.learn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Component
@Data
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String phonenumber;

}
