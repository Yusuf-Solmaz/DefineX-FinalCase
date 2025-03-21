package com.yms.projectservice.mapper;

import com.yms.projectservice.dto.request.ProjectCreateRequest;
import com.yms.projectservice.dto.response.ProjectResponse;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.entity.ProjectStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "status", target = "projectStatus")
    @Mapping(source = "deleted", target = "isDeleted")
    ProjectResponse toProjectDto(Project project);

    @Mapping(source = "projectStatus", target = "status")
    Project toProject(ProjectResponse projectResponse);

    @Mapping(source = "projectStatus", target = "status")
    Project toProject(ProjectCreateRequest projectCreateRequest);

    default String mapProjectStatusToString(ProjectStatus status) {
        return status != null ? status.getProjectStatus() : null;
    }

    default ProjectStatus mapStringToProjectStatus(String status) {
        for (ProjectStatus projectStatus : ProjectStatus.values()) {
            if (projectStatus.name().equalsIgnoreCase(status)) {
                return projectStatus;
            }
        }
        return null;
    }

}


