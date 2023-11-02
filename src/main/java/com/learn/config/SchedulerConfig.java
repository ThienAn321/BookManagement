package com.learn.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.learn.service.VerificationTokenService;

@Component
public class SchedulerConfig {
    
    @Autowired
    private VerificationTokenService verificationTokenService;

    Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    @Scheduled(fixedRate = 10000L, initialDelay = 10000L)
    public void deleteAllTokenExpiredAndVerify() {
        verificationTokenService.deleteAllExpiredSince(Instant.now());
        verificationTokenService.deleteAllVerifyToken();
        logger.info("Delete all token verify and expired : " + Instant.now());
    }

}
