package com.yms.apigateway.exception;

public class UnouthorizedEntry extends RuntimeException {
    public UnouthorizedEntry(String message) {
        super(message);
    }
}
