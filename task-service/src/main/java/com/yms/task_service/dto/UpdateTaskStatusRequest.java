package com.yms.task_service.dto;

import com.yms.task_service.entity.TaskStatus;

public record UpdateTaskStatusRequest(
        TaskStatus status,
        String reason
) {
}
