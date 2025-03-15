package com.yms.task_service.entity;

public enum TaskStatus {
    BACKLOG("Backlog"),
    IN_ANALYSIS("In Analysis"),
    IN_PROGRESS("In Progress"),
    BLOCKED("Blocked"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),;

    private final String value;


    TaskStatus(String value) {
        this.value = value;
    }

    public String getTaskStatus() {
        return value;
    }
}
