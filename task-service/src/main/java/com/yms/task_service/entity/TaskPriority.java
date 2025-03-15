package com.yms.task_service.entity;

public enum TaskPriority {
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM ("Medium"),
    LOW("Low");

    private final String value;


    TaskPriority(String value) {
        this.value = value;
    }

    public String getTaskPriority() {
        return value;
    }
}
