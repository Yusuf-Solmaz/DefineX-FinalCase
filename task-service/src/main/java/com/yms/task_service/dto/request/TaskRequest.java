package com.yms.task_service.dto.request;

import lombok.Builder;
import java.util.List;

@Builder
public record TaskRequest(
        String title,
        String description,
        Integer projectId,
        List<Integer> assigneeId,
        String priority,
        String status,
        String reason
) { }


