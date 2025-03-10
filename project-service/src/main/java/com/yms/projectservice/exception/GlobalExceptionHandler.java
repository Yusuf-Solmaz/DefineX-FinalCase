package com.yms.projectservice.exception;

import com.yms.projectservice.exception.root_exception.RootException;
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

    @ExceptionHandler(ProjectNotFound.class)
    public ResponseEntity<RootException> handleException(ProjectNotFound e) {
        RootException exception = new RootException(
                HttpStatus.NOT_FOUND.value(),
                e.getLocalizedMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }
}
