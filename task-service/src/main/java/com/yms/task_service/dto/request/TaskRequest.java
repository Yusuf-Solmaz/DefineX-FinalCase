package com.yms.task_service.dto.request;

import java.util.List;

public record TaskRequest(
        String title,
        String description,
        Integer projectId,
        List<Integer> assigneeId,
        String priority,
        String status,
        String reason
) { }


