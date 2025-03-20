package com.yms.projectservice.exception;

import com.yms.projectservice.exception.root_exception.RootException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(NoMembersFoundException.class)
    public ResponseEntity<RootException> handleException(NoMembersFoundException e) {

        RootException exception = new RootException(
                HttpStatus.NOT_FOUND.value(),
                e.getLocalizedMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RootException> handleValidationExceptions(MethodArgumentNotValidException ex) {

        RootException exception = new RootException();

        ex.getBindingResult().getAllErrors().forEach(error -> {

            String errorMessage = error.getDefaultMessage();

            exception.setError(errorMessage);
            exception.setStatus(HttpStatus.BAD_REQUEST.value());
        });


        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RootException> handleConstraintViolationExceptions(ConstraintViolationException ex) {

        RootException exception = new RootException();

        ex.getConstraintViolations().forEach(violation -> {

            String errorMessage = violation.getMessage();

            exception.setError(errorMessage);
            exception.setStatus(HttpStatus.BAD_REQUEST.value());

        });

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }


}
