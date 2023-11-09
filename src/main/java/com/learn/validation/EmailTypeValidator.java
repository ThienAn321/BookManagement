package com.learn.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailTypeValidator implements ConstraintValidator<ValidateEmail, String> {
    
    private static final String EMAIL_PATTERN = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

}
