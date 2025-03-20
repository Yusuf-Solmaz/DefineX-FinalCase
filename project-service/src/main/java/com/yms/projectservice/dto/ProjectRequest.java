package com.yms.projectservice.dto;

import java.util.List;
public record ProjectRequest(
        String title,
        String description,
        List<Integer> teamMemberIds,
        String departmentName,
        String projectStatus
) {
}
