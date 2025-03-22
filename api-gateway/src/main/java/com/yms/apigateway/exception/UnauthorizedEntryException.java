package com.yms.apigateway.exception;

public class UnauthorizedEntryException extends RuntimeException {
    public UnauthorizedEntryException(String message) {
        super(message);
    }
}
