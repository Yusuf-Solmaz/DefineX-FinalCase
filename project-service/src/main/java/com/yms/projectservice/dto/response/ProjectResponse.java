package com.yms.projectservice.dto.response;

import lombok.Builder;


@Builder
public record ProjectResponse(
        Long id,
        String title,
        String description,
        String departmentName,
        String projectStatus,
        boolean isDeleted
) {
}
