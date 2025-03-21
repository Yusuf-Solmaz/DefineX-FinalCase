package com.yms.task_service.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record TaskUpdateRequest(
        String title,
        String description,
        String priority,
        List<Integer> assigneeId,
        String reason
) {

}