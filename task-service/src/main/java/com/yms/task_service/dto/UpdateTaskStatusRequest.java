package com.yms.task_service.dto;

import com.yms.task_service.entity.TaskStatus;
import lombok.Builder;

@Builder
public record UpdateTaskStatusRequest(
        TaskStatus status,
        String reason
) {
}
