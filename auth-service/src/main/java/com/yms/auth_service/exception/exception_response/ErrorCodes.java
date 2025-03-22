package com.yms.auth_service.exception.exception_response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCodes {
    NO_CODE(501, NOT_IMPLEMENTED, "Functionality not implemented"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled. Please check email and confirm."),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
    NOT_FOUND(404,HttpStatus.NOT_FOUND, "The requested resource was not found"),
    VALIDATION_FAILED(400, HttpStatus.BAD_REQUEST, "Validation failed"),
    CONSTRAINT_VIOLATION(400, HttpStatus.BAD_REQUEST, "Constraint violation error"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "User not found"),
    ROLE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Role not found"),
    OPERATION_NOT_PERMITTED(403, HttpStatus.FORBIDDEN, "Operation not permitted"),
    ACTIVATION_TOKEN_EXCEPTION(400, HttpStatus.BAD_REQUEST, "Activation token is invalid or expired"),
    EMAIL_IN_USE(409, HttpStatus.CONFLICT, "Email is already in use"),
    USER_NOT_AUTHENTICATED(401, HttpStatus.UNAUTHORIZED, "User is not authenticated");


    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
