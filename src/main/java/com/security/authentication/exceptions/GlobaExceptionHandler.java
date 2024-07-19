package com.security.authentication.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobaExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFoundException(UserNotFoundException userNotFoundException) {
        return "User not found: " + userNotFoundException.getMessage();
    }
}
