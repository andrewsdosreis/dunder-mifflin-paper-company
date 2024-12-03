package com.andrewsreis.dundermifflin.app.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        HttpStatus httpStatus,
        String message
) {
}
