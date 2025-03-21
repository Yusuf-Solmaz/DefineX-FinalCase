package com.yms.projectservice.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectUpdateRequest(
        String title,
        String description,
        List<Integer> teamMemberIds,
        String departmentName,
        String projectStatus
) {
}
