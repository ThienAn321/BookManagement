package com.learn.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.learn.config.TwilioConfig;
import com.learn.exception.DataNotFoundException;
import com.learn.model.SmsOTP;
import com.learn.model.User;
import com.learn.repository.SmsOTPRepository;
import com.learn.repository.UserRepository;
import com.learn.service.SmsSenderService;
import com.learn.service.dto.ChangephoneRequestDTO;
import com.learn.util.OTPUtil;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsSenderServiceImpl implements SmsSenderService {

    @Value("${sms.expire}")
    private int expireTime;

    @Autowired
    private SmsOTPRepository smsOTPRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwilioConfig twilioConfig;

    @Autowired
    private OTPUtil otpUtil;

    @Override
    public String sendSMS(ChangephoneRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new DataNotFoundException(""));
        String otp = otpUtil.generateOTP();
        Instant otpExpire = Instant.now().plus(expireTime, ChronoUnit.MINUTES);
        SmsOTP smsOTP = SmsOTP.builder().otp(otp).user(user).expireAt(otpExpire).isVerify(false).isVerify(false)
                .build();
        smsOTPRepository.save(smsOTP);

        PhoneNumber sendTo = new PhoneNumber(request.getNewPhoneNumber());
        PhoneNumber sendFrom = new PhoneNumber(twilioConfig.getPhonenumber());
        String message = "Test from twilio";
        Message.creator(sendTo, sendFrom, message);
        System.out.println(sendTo);
        System.out.println(sendFrom);
        return sendTo.toString();
    }

}
