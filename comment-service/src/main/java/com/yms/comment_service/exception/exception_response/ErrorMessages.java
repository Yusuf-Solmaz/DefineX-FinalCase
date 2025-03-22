package com.yms.comment_service.exception.exception_response;

public class ErrorMessages {
    public static final String TASK_SERVICE_UNAVAILABLE = "Task service unavailable.";
    public static final String TASK_NOT_FOUND = "Task with ID %d not found!";
    public static final String COMMENT_NOT_FOUND = "Comment with ID %s not found!";
    public static final String DELETED_COMMENT_UPDATE = "Deleted comment can not be updated.";
    public static final String CONSTRAINT_VIOLATION = "Constraint violation errors occurred.";
    public static final String VALIDATION_ERROR = " Validation errors occurred.";


    private ErrorMessages() {
    }
}
