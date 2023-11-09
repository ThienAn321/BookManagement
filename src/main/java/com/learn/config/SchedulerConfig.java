package com.learn.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learn.repository.VerificationTokenRepository;

@Component
public class SchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Scheduled(fixedRate = 30000000L, initialDelay = 10000000L)
    @Transactional
    public void deleteAllTokenExpiredAndVerify() {
        verificationTokenRepository.deleteAllExpiredSince(Instant.now());
        logger.info("Delete all token verify and expired at {}: ", Instant.now());
    }

}
