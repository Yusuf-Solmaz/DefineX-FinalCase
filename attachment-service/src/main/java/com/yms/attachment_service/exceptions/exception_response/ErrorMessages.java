package com.yms.attachment_service.exceptions.exception_response;

public class ErrorMessages {
    public static final String TASK_NOT_FOUND = "Task with ID %d not found!";
    public static final String PROJECT_SERVICE_UNAVAILABLE = "Project service unavailable.";
    public static final String FILE_NOT_FOUND = "File name %s not found! (Task with ID %d)";
    public static final String FILE_COULD_NOT_READ = "File could not be read! Error: %s";
    public static final String FILE_COULD_NOT_DELETE = "File could not be deleted! Error: %s";
    public static final String FILE_COULD_NOT_SAVE = "File could not be saved! Error: %s";

    private ErrorMessages() {
    }
}
