package com.yms.projectservice.service;

import com.yms.projectservice.client.MemberClient;
import com.yms.projectservice.dto.PagedResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ProjectDto save(Project project,String token) {

        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.BACKLOG);
        }

        validateTeamMembers(project.getTeamMemberIds(),token);

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
    public PagedResponse<ProjectDto> findAll(Pageable pageable) {
        Page<ProjectDto> projects = projectRepository.findAll(pageable)
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
    public PagedResponse<ProjectDto> getAllActiveProjects(Pageable pageable) {
        Page<ProjectDto> projects = projectRepository.findAllActives(pageable)
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
    public List<ProjectDto> findByDepartmentName(String name) {
        List<ProjectDto> projects = projectRepository.findAllByDepartmentName(name)
                .stream()
                .map(projectMapper::toProjectDto)
                .toList();
        if (projects.isEmpty()) {
            throw new ProjectNotFound("Project with name " + name + " not found!");
        }
        return projects;
    }


    private void validateTeamMembers(List<Integer> teamMemberIds,String token) {
        if (teamMemberIds == null || teamMemberIds.isEmpty()) {
            return; // Boşsa doğrulama yapmaya gerek yok
        }

        // Mevcut kullanıcıları al
        List<UserResponse> existingMembers = memberClient.findUsersByIds(teamMemberIds, token);

        // Gelen yanıtı ID listesine çevir
        List<Integer> existingIds = existingMembers.stream()
                .map(UserResponse::id)
                .toList();

        // Olmayan üyeleri belirle
        List<Integer> invalidIds = teamMemberIds.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();

        if (!invalidIds.isEmpty()) {
            throw new NoMembersFoundException("Invalid team member IDs: " + invalidIds);
        }
    }

}
