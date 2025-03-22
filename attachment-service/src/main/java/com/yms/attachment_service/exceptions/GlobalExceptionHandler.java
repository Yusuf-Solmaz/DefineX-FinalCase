package com.yms.attachment_service.exceptions;

import com.yms.attachment_service.exceptions.exception_response.ErrorCodes;
import com.yms.attachment_service.exceptions.exception_response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(FileNotFoundException e) {
        ErrorCodes errorCode = ErrorCodes.FILE_NOT_FOUND;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
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
}