package com.yms.comment_service.exception;

import com.yms.comment_service.exception.exception_response.ErrorCodes;
import com.yms.comment_service.exception.exception_response.ErrorMessages;
import com.yms.comment_service.exception.exception_response.ExceptionResponse;
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

    @ExceptionHandler(DeletedCommentUpdateException.class)
    public ResponseEntity<ExceptionResponse> handleException(DeletedCommentUpdateException e) {
        ErrorCodes errorCode = ErrorCodes.DELETED_COMMENT_UPDATE;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception);
    }


    @ExceptionHandler(TaskServiceUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleException(TaskServiceUnavailableException e) {
        ErrorCodes errorCode = ErrorCodes.TASK_SERVICE_UNAVAILABLE;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
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

    @ExceptionHandler(CommentNotFound.class)
    public ResponseEntity<ExceptionResponse> handleException(CommentNotFound e) {
        ErrorCodes errorCode = ErrorCodes.COMMENT_NOT_FOUND;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception);
    }
}