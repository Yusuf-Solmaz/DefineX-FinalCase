package com.yms.projectservice.service.abstracts;

import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.dto.MemberResponse;
import com.yms.projectservice.entity.Project;

import java.util.List;

public interface ProjectService {

    ProjectDto findById(Long id);
    ProjectDto save(Project project);
    void deleteById(Long id);
    List<ProjectDto> findAll();
    List<MemberResponse> getAllMembers(Long projectId);
    List<Long> getAllMembersId(Long id);

}
