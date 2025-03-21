package com.yms.task_service.dto;

import lombok.Builder;

@Builder
public record ProjectResponse
        (
                Long id,
                String title,
                String description,
                String departmentName,
                String projectStatus,
                boolean isDeleted
        ) {
}
