package com.yms.userservice.exceptions;

import com.yms.userservice.exceptions.root_exception.RootException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler()
    public ResponseEntity<RootException> handleException(Exception e) {
        RootException exception = new RootException(
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RootException> handleException(UserNotFoundException e) {
        RootException exception = new RootException(
                HttpStatus.NOT_FOUND.value(),
                e.getLocalizedMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<RootException> handleEmailAlreadyUsedException(EmailAlreadyUsedException e) {
        RootException exception = new RootException(
                HttpStatus.BAD_GATEWAY.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(exception, HttpStatus.BAD_GATEWAY);
    }
}
