package com.learn.validation;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneTypeValidator implements ConstraintValidator<ValidatePhone, String> {

    private final Logger logger = LoggerFactory.getLogger(PhoneTypeValidator.class);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return isPhoneNumberValid(phone);
    }

    private boolean isPhoneNumberValid(String phone) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumber phoneNumber = null;
        try {

            phoneNumber = phoneUtil.parse(phone, "VN");
        } catch (NumberParseException ex) {
            logger.warn("Fail to parse phone at : {}", Instant.now());
        }
        return phoneUtil.isValidNumber(phoneNumber);
    }

}
