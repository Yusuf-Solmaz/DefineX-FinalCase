package com.yms.task_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record TaskUpdateRequest(
        @Size(min = 3, max = 100, message = "Project title must be between 3 and 100 characters.")
        String title,

        @Size(min = 10, message = "Project description must be at least 10 characters long.")
        String description,

        String priority,
        List<Integer> assigneeId,
        String reason
) {

}