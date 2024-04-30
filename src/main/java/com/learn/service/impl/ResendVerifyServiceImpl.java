package com.learn.service.impl;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.learn.exception.DataInvalidException;
import com.learn.exception.DataNotFoundException;
import com.learn.model.User;
import com.learn.model.VerificationToken;
import com.learn.model.enumeration.TitleToken;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.ResendVerifyService;
import com.learn.service.VerificationTokenService;
import com.learn.service.dto.request.EmailRequestDTO;
import com.learn.service.dto.response.MessageDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResendVerifyServiceImpl implements ResendVerifyService {

    private final Logger logger = LoggerFactory.getLogger(ResendVerifyServiceImpl.class);

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final VerificationTokenService verificationTokenService;

    private final MessageSource message;

    private User checkVerify(EmailRequestDTO request, TitleToken typeEmail) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new DataNotFoundException("email",
                message.getMessage("email.notfound", null, Locale.getDefault()), "email.error.invalid"));

        if (typeEmail.equals(TitleToken.CONFIRM_ACCOUNT) && user.getUserStatus().equals(UserStatus.ACTIVATED)) {
            logger.warn("Email {} is already actived", request.getEmail());
            throw new DataInvalidException("", message.getMessage("email.alreadyactive", null, Locale.getDefault()),
                    "email.alreadyactive");
        }

        Optional<VerificationToken> verificationToken = verificationTokenRepository
                .findVerifyTokenByEmail(user.getEmail()).stream()
                .filter(token -> token.getTitleToken().equals(typeEmail)).findFirst();

        if (verificationToken.isPresent()) {
            if (!verificationTokenService.isExpire(verificationToken.get().getToken(), verificationToken.get().getOtp(),
                    typeEmail)) {
                logger.warn("Link verify or otp is not expired");
                throw new DataInvalidException("", message.getMessage("verify.notexpire", null, Locale.getDefault()),
                        "verify.notexpire");
            }
            logger.info("Verification token id {} has been deleted at : {}", verificationToken.get().getId(),
                    Instant.now());
            verificationTokenRepository.deleteById(verificationToken.get().getId());
        }
        return user;
    }

    @Override
    public MessageDTO resendVerifyAccount(EmailRequestDTO request) {
        User user = checkVerify(request, TitleToken.CONFIRM_ACCOUNT);
        verificationTokenService.sendVerificationToken(user, null, TitleToken.CONFIRM_ACCOUNT);
        logger.info("Resend user {} a confirm token at : {}", user.getEmail(), Instant.now());
        return MessageDTO.builder().status(HttpStatus.OK.value())
                .message(message.getMessage("resend.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    @Override
    public MessageDTO resendChangeEmail(EmailRequestDTO request) {
        User user = checkVerify(request, TitleToken.CHANGE_EMAIL);
        verificationTokenService.sendVerificationToken(user, null, TitleToken.CHANGE_EMAIL);
        logger.info("Resend user {} a change email verify token at : {}", user.getEmail(), Instant.now());
        return MessageDTO.builder().status(HttpStatus.OK.value())
                .message(message.getMessage("resend.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

}
