package com.yms.task_service.exception;

import com.yms.task_service.exception.exception_response.ErrorCodes;
import com.yms.task_service.exception.exception_response.ErrorMessages;
import com.yms.task_service.exception.exception_response.ExceptionResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashSet;
import java.util.Set;

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

    @ExceptionHandler(InvalidPriorityException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidPriorityException e) {
        ErrorCodes errorCode = ErrorCodes.INVALID_PRIORITY;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception);
    }


    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserServiceUnavailableException e) {
        ErrorCodes errorCode = ErrorCodes.USER_SERVICE_UNAVAILABLE;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(exception);
    }

    @ExceptionHandler(ProjectServiceUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleException(ProjectServiceUnavailableException e) {
        ErrorCodes errorCode = ErrorCodes.PROJECT_SERVICE_UNAVAILABLE;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(exception);
    }

    @ExceptionHandler(InvalidAssigneesException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidAssigneesException e) {
        ErrorCodes errorCode = ErrorCodes.INVALID_PRIORITY;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(TaskNotFoundException e) {
        ErrorCodes errorCode = ErrorCodes.TASK_NOT_FOUND;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(ProjectNotFoundException e) {
        ErrorCodes errorCode = ErrorCodes.PROJECT_NOT_FOUND;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception);
    }

    @ExceptionHandler(NoMembersFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(NoMembersFoundException e) {
        ErrorCodes errorCode = ErrorCodes.NO_MEMBERS_FOUND;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception);
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