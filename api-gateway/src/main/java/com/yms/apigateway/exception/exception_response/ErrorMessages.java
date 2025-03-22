package com.yms.apigateway.exception.exception_response;

public class ErrorMessages {
    public static final String UNAUTHORIZED_ENTRY = "User does not have sufficient roles to access this resource";
    public static final String INVALID_TOKEN = "The provided token is invalid!";

    private ErrorMessages() {
    }
}
