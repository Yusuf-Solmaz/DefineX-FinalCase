package com.yms.projectservice.service.abstracts;

import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.dto.MemberResponse;
import com.yms.projectservice.entity.Project;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProjectService {

    ProjectDto findById(Long id);
    ProjectDto save(Project project);
    void deleteById(Long id);
    List<ProjectDto> findAll();
    List<MemberResponse> getAllMembers(Authentication connectedUser,Long projectId);
    List<Long> getAllMembersId(Long id);

}
