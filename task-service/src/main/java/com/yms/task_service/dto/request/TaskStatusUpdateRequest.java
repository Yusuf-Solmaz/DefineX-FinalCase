package com.yms.task_service.dto.request;

import com.yms.task_service.entity.TaskStatus;
import lombok.Builder;

@Builder
public record TaskStatusUpdateRequest(
        TaskStatus status,
        String reason
) {
}
