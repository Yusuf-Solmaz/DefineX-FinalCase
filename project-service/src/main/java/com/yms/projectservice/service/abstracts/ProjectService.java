package com.yms.projectservice.service.abstracts;

import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.dto.UserResponse;
import com.yms.projectservice.entity.Project;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProjectService {

    ProjectDto findById(Integer id);
    ProjectDto save(Project project);
    void deleteById(Integer id);
    List<ProjectDto> findAll();
    List<UserResponse> getAllMembers(Integer projectId, String token);
    List<Integer> getAllMembersId(Integer id);

}
