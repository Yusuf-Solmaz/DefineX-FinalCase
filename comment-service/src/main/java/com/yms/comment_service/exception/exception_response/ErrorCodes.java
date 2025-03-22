package com.yms.comment_service.exception.exception_response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {
    NO_CODE(501, HttpStatus.NOT_IMPLEMENTED, "Functionality not implemented"),
    TASK_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Task not found"),
    COMMENT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Comment not found"),
    VALIDATION_FAILED(400, HttpStatus.BAD_REQUEST, "Validation failed"),
    CONSTRAINT_VIOLATION(400, HttpStatus.BAD_REQUEST, "Constraint violation error"),
    USER_SERVICE_UNAVAILABLE(503, HttpStatus.SERVICE_UNAVAILABLE, "User service unavailable"),
    TASK_SERVICE_UNAVAILABLE(503, HttpStatus.SERVICE_UNAVAILABLE, "Task service unavailable"),
    DELETED_COMMENT_UPDATE(400, HttpStatus.BAD_REQUEST, "Deleted comment can not be updated");


    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}

