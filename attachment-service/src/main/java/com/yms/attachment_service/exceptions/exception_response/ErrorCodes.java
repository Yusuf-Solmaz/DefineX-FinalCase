package com.yms.attachment_service.exceptions.exception_response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {
    NO_CODE(501, HttpStatus.NOT_IMPLEMENTED, "Functionality not implemented"),
    TASK_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Task not found"),
    FILE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "File not found"),
    PROJECT_SERVICE_UNAVAILABLE(503, HttpStatus.SERVICE_UNAVAILABLE, "Project service unavailable");


    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}

