package com.yms.projectservice.entity;

public enum ProjectStatus {
    BACKLOG("Backlog"),
    IN_ANALYSIS("In Analysis"),
    IN_PROGRESS("In Progress"),
    CANCELLED("Cancelled"),
    BLOCKED("Blocked"),
    COMPLETED("Completed");

    private final String value;


    ProjectStatus(String value) {
        this.value = value;
    }

    public String getProjectStatus() {
        return value;
    }
}
