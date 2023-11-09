package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.exception.DataNotFoundException;
import com.learn.exception.DataUnauthorizedException;
import com.learn.model.User;
import com.learn.model.UserSession;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.repository.UserSessionRepository;
import com.learn.service.AuthenticationService;
import com.learn.service.JwtService;
import com.learn.service.dto.AuthenticationRequestDTO;
import com.learn.service.dto.AuthenticationResponseDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private static final int MAX_FAILED_ATTEMPTS = 3;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource message;

    @Override
    @Transactional(dontRollbackOn = DataUnauthorizedException.class)
    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new DataNotFoundException(message.getMessage("email.notfound", null, Locale.getDefault())));

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException ex1) {
            logger.error("User {} with status {} failed to login at : {}", user.getEmail(), user.getUserStatus(),
                    Instant.now());
            throw new DataUnauthorizedException(message.getMessage("user.unauthorized", null, Locale.getDefault()));
        } catch (BadCredentialsException ex2) {
            increaseFailedAttempts(user);
            logger.warn("User {} login failed at {}", user.getEmail(), Instant.now());
            throw new DataUnauthorizedException(message.getMessage("password.notmatch", null, Locale.getDefault()));
        } catch (LockedException ex3) {
            throw new DataUnauthorizedException(message.getMessage("user.lock30m", null, Locale.getDefault()));
        }

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            resetTime(user);
            logger.info("User {} has been login successfully at : {}", user.getEmail(), Instant.now());
        }

        Instant expireDate = Instant.now().plus(60, ChronoUnit.MINUTES);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateToken(userDetails, null, false);
        String jti = jwtService.getId(accessToken);
        String refreshToken = jwtService.generateToken(userDetails, jti, true);
        UserSession userSession = UserSession.builder().sessionID(jti).isActive(true).expireAt(expireDate).user(user)
                .build();
        userSessionRepository.save(userSession);
        logger.info("Generated user session for user {} at : {}", user.getEmail(), Instant.now());
        return AuthenticationResponseDTO.builder().token(accessToken).refreshToken(refreshToken).build();
    }

    private void increaseFailedAttempts(User user) {
        if (user.getFailedAttempt() + 1 > MAX_FAILED_ATTEMPTS) {
            user.setLockTime(Instant.now().plus(30, ChronoUnit.MINUTES));
            userRepository.save(user);
        } else {
            int newIncrease = user.getFailedAttempt() + 1;
            userRepository.updateFailedAttempts(newIncrease, user.getEmail());
        }
    }

    private void resetTime(User user) {
        user.setUserStatus(UserStatus.ACTIVATED);
        user.setFailedAttempt(0);
        user.setLockTime(null);
        userRepository.save(user);
    }

}
