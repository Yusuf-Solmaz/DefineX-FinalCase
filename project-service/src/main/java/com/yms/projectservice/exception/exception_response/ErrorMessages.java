package com.yms.projectservice.exception.exception_response;

public class ErrorMessages {
    public static final String PROJECT_NOT_FOUND = "Project with ID %d not found!";
    public static final String NO_MEMBERS_FOUND = "No members found in the project.";
    public static final String INVALID_PROJECT_STATUS = "Invalid project status value: %s";
    public static final String INVALID_TEAM_MEMBER = "Invalid team member IDs: ";
    public static final String PROJECT_STATUS_TRANSITION_INVALID = "Invalid state transition from %s to %s.";
    public static final String USER_SERVICE_UNAVAILABLE = "User service unavailable.";
    public static final String CHANGE_TASK_COMPLETED = "Cannot change status of a completed project.";
    public static final String CONSTRAINT_VIOLATION = "Constraint violation errors occurred.";
    public static final String VALIDATION_ERROR = " Validation errors occurred.";

    private ErrorMessages() {
    }
}
