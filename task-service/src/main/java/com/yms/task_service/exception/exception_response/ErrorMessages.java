package com.yms.task_service.exception.exception_response;

public class ErrorMessages {

    public static final String TASK_NOT_FOUND = "Task with ID %d not found!";
    public static final String PROJECT_NOT_FOUND = "Project with ID %d not found!";
    public static final String NO_MEMBERS_FOUND = "No members found in the project.";
    public static final String INVALID_ASSIGNEE = "The following assignees are not part of the project team: %s";
    public static final String PROJECT_DELETED = "Project was deleted.";
    public static final String CHANGE_TASK_COMPLETED = "Cannot change status of a completed task.";
    public static final String UPDATE_TASK_COMPLETED = "Completed tasks cannot be updated.";
    public static final String TASK_CANCELLED_REASON_REQUIRED = "A reason must be provided for CANCELLED or BLOCKED status.";
    public static final String INVALID_PRIORITY = "Invalid priority value: %s";
    public static final String TASK_STATUS_TRANSITION_INVALID = "Invalid state transition from %s to %s.";
    public static final String TASK_BLOCKED_INVALID_STATE = "Tasks can only be blocked if they are in IN_ANALYSIS or IN_PROGRESS.";
    public static final String PROJECT_TASKS_NOT_FOUND = "Project with ID %d has no tasks.";
    public static final String TASK_REASON_NOT_PROVIDED = "No reason provided.";
    public static final String INVALID_ASSIGNEES = "The following assignees are not part of the project team: ";
    public static final String PROJECT_SERVICE_UNAVAILABLE = "Project service unavailable.";
    public static final String USER_SERVICE_UNAVAILABLE = "User service unavailable.";



    private ErrorMessages() {
    }
}
