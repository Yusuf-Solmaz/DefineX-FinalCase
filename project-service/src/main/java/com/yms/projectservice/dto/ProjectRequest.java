package com.yms.projectservice.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ProjectRequest(
        String title,
        String description,
        List<Integer> teamMemberIds,
        String departmentName,
        String projectStatus
) {
}
