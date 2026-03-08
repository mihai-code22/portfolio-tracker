package com.portfolio.tracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;

        List<String> violations = new ArrayList<>();

        if (password.length() < 8)
            violations.add("Password must be at least 8 characters");
        if (!password.matches(".*[A-Z].*"))
            violations.add("Password must contain at least one uppercase letter");
        if (!password.matches(".*[a-z].*"))
            violations.add("Password must contain at least one lowercase letter");
        if (!password.matches(".*\\d.*"))
            violations.add("Password must contain at least one digit");
        if (!password.matches(".*[@$!%*?&].*"))
            violations.add("Password must contain at least one special character (@$!%*?&)");

        if (violations.isEmpty()) return true;

        context.disableDefaultConstraintViolation();
        for (String violation : violations) {
            context.buildConstraintViolationWithTemplate(violation).addConstraintViolation();
        }
        return false;
    }
}
