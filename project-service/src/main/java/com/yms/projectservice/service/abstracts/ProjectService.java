package com.yms.projectservice.service.abstracts;

import com.yms.projectservice.dto.PagedResponse;
import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.dto.UserResponse;
import com.yms.projectservice.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProjectService {

    ProjectDto findById(Integer id);
    ProjectDto save(Project project,String token);
    void deleteById(Integer id);
    PagedResponse<ProjectDto> findAll(Pageable pageable);
    List<UserResponse> getAllMembers(Integer projectId, String token);
    List<Integer> getAllMembersId(Integer id);

    List<ProjectDto> findByDepartmentName(String name);

    PagedResponse<ProjectDto> getAllActiveProjects(Pageable pageable);
}
