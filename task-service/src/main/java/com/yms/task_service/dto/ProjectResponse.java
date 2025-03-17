package com.yms.task_service.dto;

public record ProjectResponse
        (
                Long id,
                String title,
                String description,
                String departmentName,
                String projectStatus
        ) {
}
