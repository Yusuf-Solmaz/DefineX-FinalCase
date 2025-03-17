package com.yms.projectservice.service;

import com.yms.projectservice.client.MemberClient;
import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.dto.UserResponse;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.entity.ProjectStatus;
import com.yms.projectservice.exception.NoMembersFoundException;
import com.yms.projectservice.exception.ProjectNotFound;
import com.yms.projectservice.mapper.ProjectMapper;
import com.yms.projectservice.repository.ProjectRepository;
import com.yms.projectservice.service.abstracts.MemberClientService;
import com.yms.projectservice.service.abstracts.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final MemberClientService memberClient;

    @Override
    public ProjectDto findById(Integer id) {
        return projectRepository.findById(id)
                .map(projectMapper::toProjectDto)
                .orElseThrow(() -> new ProjectNotFound("Project with ID " + id + " not found!"));
    }

    @Override
    public ProjectDto save(Project project) {

        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.BACKLOG);
        }

        return projectMapper.toProjectDto(projectRepository.save(project));
    }

    @Override
    public void deleteById(Integer id) {
        try{
            projectRepository.deleteById(id);
        } catch (ProjectNotFound e) {
            throw new ProjectNotFound("Project not found!");
        }
    }

    @Override
    public List<ProjectDto> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toProjectDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllMembers(Integer projectId, String token) {
        List<Integer> memberIds = projectRepository.findById(projectId)
                .stream()
                .flatMap(project -> project.getTeamMemberIds().stream())
                .distinct()
                .toList();


        if (memberIds.isEmpty()) {
            throw new NoMembersFoundException("No members found in the project.");
        }

        System.out.println("Token: " + token);

        return memberClient.findUsersByIds(memberIds, token);
    }

    @Override
    public List<Integer> getAllMembersId(Integer id) {
        return projectRepository.findMemberIdsByProjectId(id);
    }


}
