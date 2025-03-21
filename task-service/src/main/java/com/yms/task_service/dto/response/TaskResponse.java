package com.yms.task_service.dto.response;

import lombok.Builder;

@Builder
public record TaskResponse(
        Integer id,
        String title,
        String description,
        String priority,
        String status,
        String reason
) {
}
