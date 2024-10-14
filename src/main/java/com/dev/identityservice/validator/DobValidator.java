package com.dev.identityservice.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        if (Objects.isNull(value)) return true;

        long year = ChronoUnit.YEARS.between(value, LocalDate.now());

        // if (!(year >= min)) {
        //     context.disableDefaultConstraintViolation();
        //     context.buildConstraintViolationWithTemplate(ErrorCode.INVALID_DOB.getMeassge().replace("{min}",
        // String.valueOf(min)))
        //            .addConstraintViolation();
        // }

        return year >= min;
    }

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
