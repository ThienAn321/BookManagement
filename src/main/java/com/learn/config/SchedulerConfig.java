package com.learn.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.learn.repository.VerificationTokenRepository;

@Component
public class SchedulerConfig {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    @Scheduled(fixedRate = 1000000L, initialDelay = 1000000L)
    public void deleteAllTokenExpiredAndVerify() {
        verificationTokenRepository.deleteAllExpiredSince(Instant.now());
        logger.info("Delete all token verify and expired {}: ", Instant.now());
    }

}
