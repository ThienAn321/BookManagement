package com.learn.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.learn.exception.DataInvalidException;
import com.learn.exception.DataNotFoundException;
import com.learn.model.User;
import com.learn.model.VerificationToken;
import com.learn.model.enumeration.UserStatus;
import com.learn.repository.UserRepository;
import com.learn.repository.VerificationTokenRepository;
import com.learn.service.ResendVerifyService;
import com.learn.service.VerificationTokenService;
import com.learn.service.dto.ObjectDTO;
import com.learn.service.dto.RefreshRequestDTO;

@Service
public class ResendVerifyServiceImpl implements ResendVerifyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public ObjectDTO resendVerify(RefreshRequestDTO request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (!user.isPresent()) {
            throw new DataNotFoundException("{email.notfound}");
        }

        if (user.get().getUserStatus().equals(UserStatus.ACTIVATED)) {
            throw new DataInvalidException("{email.alreadyactive}");
        }

        VerificationToken verificationToken = user.get().getVerificationToken();
        if (verificationToken != null && verificationToken.getExpireAt() != null) {
            if (verificationToken.isExpire()) {
                verificationTokenRepository.deleteById(verificationToken.getId());
            } else {
                throw new DataInvalidException("{verify.notexpire}");
            }
        }
        verificationTokenService.sendVerificationToken(user.get());
        return ObjectDTO.builder().statusCode(HttpStatus.OK.value()).message("{resend.success}")
                .timestamp(Instant.now()).build();
    }

}
