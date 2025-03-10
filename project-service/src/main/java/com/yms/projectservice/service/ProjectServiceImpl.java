package com.yms.projectservice.service;

import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.entity.ProjectStatus;
import com.yms.projectservice.exception.ProjectNotFound;
import com.yms.projectservice.mapper.ProjectMapper;
import com.yms.projectservice.repository.ProjectRepository;
import com.yms.projectservice.service.abstracts.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectDto findById(Long id) {
        return null;
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
}
