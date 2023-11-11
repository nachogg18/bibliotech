package com.bibliotech.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> handleCustomException(ValidationException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ErrorDto(
                    "Error de validacion de la request",
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage(),
                            request.getRequestURI()
                ), HttpStatus.BAD_REQUEST);
    }
}