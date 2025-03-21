package com.yms.task_service.exception.exception_response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {
    NO_CODE(501, HttpStatus.NOT_IMPLEMENTED, "Functionality not implemented"),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "The requested resource was not found"),
    TASK_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Task not found"),
    PROJECT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Project not found"),
    NO_MEMBERS_FOUND(404, HttpStatus.NOT_FOUND, "No members found"),
    VALIDATION_FAILED(400, HttpStatus.BAD_REQUEST, "Validation failed"),
    CONSTRAINT_VIOLATION(400, HttpStatus.BAD_REQUEST, "Constraint violation error"),
    INVALID_PRIORITY(400, HttpStatus.BAD_REQUEST, "Invalid priority"),
    INVALID_ASSIGNEES(400, HttpStatus.BAD_REQUEST, "Invalid assignees"),
    PROJECT_SERVICE_UNAVAILABLE(503, HttpStatus.SERVICE_UNAVAILABLE, "Project service unavailable"),
    USER_SERVICE_UNAVAILABLE(503, HttpStatus.SERVICE_UNAVAILABLE, "User service unavailable");

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}

