package com.learn.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER }) // class level
@Retention(RetentionPolicy.RUNTIME) // chay vao luc nao
@Documented
@Constraint(validatedBy = PhoneTypeValidator.class) // logic validation
public @interface ValidatePhone {

    String message() default "{jakarta.validation.constraints.Phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
