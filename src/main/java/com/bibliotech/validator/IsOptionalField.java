package com.bibliotech.validator;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

//TODO
// ver si remover este validator que no funciona

@Constraint(validatedBy = IsOptionalFieldValidator.class)
@Target({METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
@Documented
public @interface IsOptionalField {
    String message() default "El campo es opcional";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
