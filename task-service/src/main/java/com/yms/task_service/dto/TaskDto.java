package com.yms.task_service.dto;

public record TaskDto(
        Integer id,
        String title,
        String description,
        String priority,
        String status,
        String reason
) {
}
