package com.yms.projectservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectUpdateRequest(

        @Size(min = 3, max = 100, message = "Project title must be between 3 and 100 characters.")
        String title,

        @Size(min = 3, max = 100, message = "Description title must be between 3 and 100 characters.")
        String description,
        List<Integer> teamMemberIds,

        @Size(min = 2, max = 100, message = "Department name title must be between 2 and 100 characters.")
        String departmentName,
        String projectStatus
) {
}
