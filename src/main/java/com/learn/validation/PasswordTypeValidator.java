package com.learn.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordTypeValidator implements ConstraintValidator<ValidatePassword, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

}
