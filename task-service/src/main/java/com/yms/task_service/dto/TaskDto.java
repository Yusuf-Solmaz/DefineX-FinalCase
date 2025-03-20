package com.yms.task_service.dto;

import lombok.Builder;

@Builder
public record TaskDto(
        Integer id,
        String title,
        String description,
        String priority,
        String status,
        String reason
) {
}
