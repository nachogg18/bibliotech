package com.bibliotech.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class IsOptionalFieldValidator
  implements ConstraintValidator<IsOptionalField, Object> {

    @Override
    public boolean isValid(
      Object anObject, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(anObject.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize(IsOptionalField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}