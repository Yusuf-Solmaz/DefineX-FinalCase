package com.yms.projectservice.mapper;

import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.entity.ProjectStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "status", target = "projectStatus")
    ProjectDto toProjectDto(Project project);

    @Mapping(source = "projectStatus", target = "status")
    Project toProject(ProjectDto projectDto);

    default String mapProjectStatusToString(ProjectStatus status) {
        return status != null ? status.getProjectStatus() : null;
    }

    default ProjectStatus mapStringToProjectStatus(String status) {
        for (ProjectStatus projectStatus : ProjectStatus.values()) {
            if (projectStatus.getProjectStatus().equalsIgnoreCase(status)) {
                return projectStatus;
            }
        }
        return null;
    }
}


