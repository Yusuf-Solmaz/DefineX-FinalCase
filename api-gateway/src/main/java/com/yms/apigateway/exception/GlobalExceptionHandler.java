package com.yms.apigateway.exception;

import com.yms.apigateway.exception.exception_response.ErrorCodes;
import com.yms.apigateway.exception.exception_response.ExceptionResponse;
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

    @ExceptionHandler(UnauthorizedEntryException.class)
    public ResponseEntity<ExceptionResponse> handleException(UnauthorizedEntryException e) {
        ErrorCodes errorCode = ErrorCodes.UNAUTHORIZED_ENTRY;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidTokenException e) {
        ErrorCodes errorCode = ErrorCodes.INVALID_TOKEN;
        ExceptionResponse exception = ExceptionResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .errorDetail(e.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception);
    }

}
