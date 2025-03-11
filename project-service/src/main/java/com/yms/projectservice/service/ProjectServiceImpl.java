package com.yms.projectservice.service;

import com.yms.projectservice.client.MemberClient;
import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.dto.MemberResponse;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.entity.ProjectStatus;
import com.yms.projectservice.exception.NoMembersFoundException;
import com.yms.projectservice.exception.ProjectNotFound;
import com.yms.projectservice.mapper.ProjectMapper;
import com.yms.projectservice.repository.ProjectRepository;
import com.yms.projectservice.service.abstracts.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final MemberClient memberClient;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, MemberClient memberClient) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.memberClient = memberClient;
    }

    @Override
    public ProjectDto findById(Long id) {
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
    public void deleteById(Long id) {
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
    public List<ProjectDto> findByMemberId(Long memberId) {
        return projectRepository.findByTeamMemberId(memberId)
                .stream()
                .map(projectMapper::toProjectDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<MemberResponse> getAllMembers(Long projectId) {
        List<Long> memberIds = projectRepository.findById(projectId)
                .stream()
                .flatMap(project -> project.getTeamMemberIds().stream())
                .distinct()
                .toList();

        System.out.println(projectRepository.findById(projectId));
        System.out.println("memberIds = " + memberIds);

        if (memberIds.isEmpty()) {
            throw new NoMembersFoundException("No members found in the project.");
        }

        return memberClient.findUsersByIds(memberIds);
    }

    @Override
    public List<Long> getAllMembersId(Long id) {
        return projectRepository.findMemberIdsByProjectId(id);
    }


}
