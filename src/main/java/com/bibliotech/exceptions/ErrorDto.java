package com.bibliotech.exceptions;

public record ErrorDto(
        String message,
        int status,
        String error,
        String path
) {
}
