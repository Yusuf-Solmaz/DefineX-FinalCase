package com.yms.auth_service.exception;
import com.yms.auth_service.exception.exception_response.ErrorCodes;
import com.yms.auth_service.exception.exception_response.ErrorMessages;
import com.yms.auth_service.exception.exception_response.ExceptionResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashSet;
import java.util.Set;

import static com.yms.auth_service.exception.exception_response.ErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler()
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        ErrorCodes errorCode = ErrorCodes.NO_CODE;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotAuthenticatedException e) {
        ErrorCodes errorCode = ErrorCodes.USER_NOT_AUTHENTICATED;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException e) {
        ErrorCodes errorCode = ErrorCodes.USER_NOT_FOUND;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception);
    }

    @ExceptionHandler(EmailInUseException.class)
    public ResponseEntity<ExceptionResponse> handleException(EmailInUseException e) {
        ErrorCodes errorCode = EMAIL_IN_USE;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(CONFLICT)
                .body(exception);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(RoleNotFoundException e) {
        ErrorCodes errorCode = ErrorCodes.ROLE_NOT_FOUND;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception);
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException e) {
        ErrorCodes errorCode = ErrorCodes.OPERATION_NOT_PERMITTED;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(FORBIDDEN)
                .body(exception);
    }

    @ExceptionHandler(ActivationTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(ActivationTokenException e) {
        ErrorCodes errorCode = ErrorCodes.ACTIVATION_TOKEN_EXCEPTION;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(exception);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ACCOUNT_LOCKED.getCode())
                                .errorDescription(ACCOUNT_LOCKED.getDescription())
                                .errorDetail(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ACCOUNT_DISABLED.getCode())
                                .errorDescription(ACCOUNT_DISABLED.getDescription())
                                .errorDetail(exp.getMessage())
                                .build()
                );
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(BAD_CREDENTIALS.getCode())
                                .errorDescription(BAD_CREDENTIALS.getDescription())
                                .errorDetail(ErrorMessages.BAD_CREDENTIALS_LOGIN)
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .errorDetail(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorCodes errorCode = ErrorCodes.VALIDATION_FAILED;
        Set<String> validationErrors = new HashSet<>();

        ex.getBindingResult().getAllErrors().forEach(error ->
                validationErrors.add(error.getDefaultMessage())
        );

        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(ErrorMessages.VALIDATION_ERROR)
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        ErrorCodes errorCode = ErrorCodes.CONSTRAINT_VIOLATION;
        Set<String> validationErrors = new HashSet<>();

        ex.getConstraintViolations().forEach(violation ->
                validationErrors.add(violation.getMessage())
        );

        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(ErrorMessages.CONSTRAINT_VIOLATION)
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception);
    }
}