package com.yms.attachment_service.dto;

public record TaskResponse(
        Integer id,
        String title,
        String description,
        String priority,
        String status,
        String reason
) {
}
