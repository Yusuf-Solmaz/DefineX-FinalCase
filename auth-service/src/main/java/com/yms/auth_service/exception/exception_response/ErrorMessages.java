package com.yms.auth_service.exception.exception_response;

public class ErrorMessages {
    public static final String CONSTRAINT_VIOLATION = "Constraint violation errors occurred.";
    public static final String VALIDATION_ERROR = " Validation errors occurred.";
    public static final String BAD_CREDENTIALS_LOGIN = "Login and / or Password is incorrect";
    public static final String ROLE_NOT_FOUND = "Role: ID %s not found!";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String TOKEN_EXPIRED ="Activation token has expired. A new token has been send to the same email address";
    public static final String USER_NOT_FOUND = "User: %d not found! ";
    public static final String EMAIL_IN_USE = "Email is already in use: %s";
    public static final String USER_NOT_AUTHENTICATED = "User is not authenticated";
    private ErrorMessages() {
    }
}
