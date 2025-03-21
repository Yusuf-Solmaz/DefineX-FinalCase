package com.yms.projectservice.service.abstracts;

import com.yms.projectservice.dto.PagedResponse;
import com.yms.projectservice.dto.ProjectResponse;
import com.yms.projectservice.dto.ProjectRequest;
import com.yms.projectservice.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    ProjectResponse findById(Integer id);
    ProjectResponse save(ProjectRequest project, String token);
    void deleteById(Integer id);
    PagedResponse<ProjectResponse> findAll(Pageable pageable);
    List<UserResponse> getAllMembers(Integer projectId, String token);
    List<Integer> getAllMembersId(Integer id);

    List<ProjectResponse> findByDepartmentName(String name);

    PagedResponse<ProjectResponse> getAllActiveProjects(Pageable pageable);
}
