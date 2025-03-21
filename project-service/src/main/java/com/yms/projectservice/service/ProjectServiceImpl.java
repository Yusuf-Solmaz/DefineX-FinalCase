package com.yms.projectservice.service;

import com.yms.projectservice.dto.request.ProjectUpdateRequest;
import com.yms.projectservice.dto.response.PagedResponse;
import com.yms.projectservice.dto.response.ProjectResponse;
import com.yms.projectservice.dto.request.ProjectCreateRequest;
import com.yms.projectservice.dto.response.UserResponse;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.entity.ProjectStatus;
import com.yms.projectservice.exception.NoMembersFoundException;
import com.yms.projectservice.exception.ProjectNotFound;
import com.yms.projectservice.mapper.ProjectMapper;
import com.yms.projectservice.repository.ProjectRepository;
import com.yms.projectservice.service.abstracts.MemberClientService;
import com.yms.projectservice.service.abstracts.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final MemberClientService memberClient;

    @Override
    public ProjectResponse findById(Integer id) {

        return projectRepository.findById(id)
                .map(projectMapper::toProjectDto)
                .orElseThrow(() -> new ProjectNotFound("Project with ID " + id + " not found!"));

    }

    @Override
    public ProjectResponse save(ProjectCreateRequest projectCreateRequest, String token) {

        Project project = projectMapper.toProject(projectCreateRequest);

        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.BACKLOG);
        }

        validateTeamMembers(project.getTeamMemberIds(), token);

        return projectMapper.toProjectDto(projectRepository.save(project));
    }

    @Override
    public void deleteById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFound("Project not found!"));
        project.setDeleted(true);
        projectRepository.save(project);
    }

    @Override
    public PagedResponse<ProjectResponse> findAll(Pageable pageable) {
        Page<ProjectResponse> projects = projectRepository.findAll(pageable)
                .map(projectMapper::toProjectDto);


        return new PagedResponse<>(
                projects.getContent(),
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages(),
                projects.isLast()
        );
    }


    @Override
    public PagedResponse<ProjectResponse> getAllActiveProjects(Pageable pageable) {
        Page<ProjectResponse> projects = projectRepository.findAllActives(pageable)
                .map(projectMapper::toProjectDto);

        return new PagedResponse<>(
                projects.getContent(),
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages(),
                projects.isLast()
        );
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


    @Override
    public List<ProjectResponse> findByDepartmentName(String name) {
        List<ProjectResponse> projects = projectRepository.findAllByDepartmentName(name)
                .stream()
                .map(projectMapper::toProjectDto)
                .toList();
        if (projects.isEmpty()) {
            throw new ProjectNotFound("Project with name " + name + " not found!");
        }
        return projects;
    }


    @Override
    public ProjectResponse updateProject(Integer id, ProjectUpdateRequest updateRequest, String token) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFound("Project with ID " + id + " not found!"));

        mergeProjectDetails(project, updateRequest, token);

        projectRepository.save(project);
        return projectMapper.toProjectDto(project);
    }

    private void mergeProjectDetails(Project project, ProjectUpdateRequest updateRequest, String token) {
        if (updateRequest.title() != null && !updateRequest.title().isEmpty()) {
            project.setTitle(updateRequest.title());
        }

        if (updateRequest.description() != null && !updateRequest.description().isEmpty()) {
            project.setDescription(updateRequest.description());
        }

        if (updateRequest.teamMemberIds() != null && !updateRequest.teamMemberIds().isEmpty()) {
            validateTeamMembers(updateRequest.teamMemberIds(), token);
            project.setTeamMemberIds(updateRequest.teamMemberIds());
        }

        if (updateRequest.departmentName() != null && !updateRequest.departmentName().isEmpty()) {
            project.setDepartmentName(updateRequest.departmentName());
        }

        if (updateRequest.projectStatus() != null) {
            ProjectStatus newStatus = projectMapper.mapStringToProjectStatus(updateRequest.projectStatus());
            if (newStatus != null) {
                validateStateTransition(project.getStatus(), newStatus);
                project.setStatus(newStatus);
            } else {
                throw new IllegalArgumentException("Invalid project status value: " + updateRequest.projectStatus());
            }
        }
    }

    private void validateTeamMembers(List<Integer> teamMemberIds,String token) {
        if (teamMemberIds == null || teamMemberIds.isEmpty()) {
            return;
        }

        List<UserResponse> existingMembers = memberClient.findUsersByIds(teamMemberIds, token);

        List<Integer> existingIds = existingMembers.stream()
                .map(UserResponse::id)
                .toList();

        List<Integer> invalidIds = teamMemberIds.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();

        if (!invalidIds.isEmpty()) {
            throw new NoMembersFoundException("Invalid team member IDs: " + invalidIds);
        }
    }

    private void validateStateTransition(ProjectStatus currentStatus, ProjectStatus newStatus) {
        if (newStatus == ProjectStatus.BLOCKED && currentStatus != ProjectStatus.IN_ANALYSIS && currentStatus != ProjectStatus.IN_PROGRESS) {
            throw new RuntimeException("Tasks can only be blocked if they are in IN_ANALYSIS or IN_PROGRESS.");
        }

        List<ProjectStatus> allowedTransitions = switch (currentStatus) {
            case BACKLOG -> List.of(ProjectStatus.IN_ANALYSIS);
            case IN_ANALYSIS -> List.of(ProjectStatus.IN_PROGRESS, ProjectStatus.BLOCKED, ProjectStatus.CANCELLED);
            case IN_PROGRESS -> List.of(ProjectStatus.COMPLETED, ProjectStatus.BLOCKED, ProjectStatus.CANCELLED);
            case BLOCKED -> List.of(ProjectStatus.IN_ANALYSIS, ProjectStatus.IN_PROGRESS);
            case CANCELLED -> List.of();
            case COMPLETED -> throw new RuntimeException("Cannot change status of a completed task.");
        };

        if (!allowedTransitions.contains(newStatus)) {
            throw new RuntimeException("Invalid state transition from " + currentStatus + " to " + newStatus);
        }
    }


}
