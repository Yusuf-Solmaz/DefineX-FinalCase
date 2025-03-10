package com.yms.projectservice.dto;

import java.util.List;

public record ProjectDto(
        Long id,
        String title,
        String description,
        String departmentName,
        String projectStatus
) {
}
