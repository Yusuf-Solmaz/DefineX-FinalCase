package com.yms.projectservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yms.projectservice.dto.request.ProjectCreateRequest;
import com.yms.projectservice.dto.response.PagedResponse;
import com.yms.projectservice.dto.response.ProjectResponse;
import com.yms.projectservice.dto.response.UserResponse;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.exception.ProjectNotFoundException;
import com.yms.projectservice.mapper.ProjectMapper;
import com.yms.projectservice.repository.ProjectRepository;
import com.yms.projectservice.service.ProjectServiceImpl;
import com.yms.projectservice.exception.NoMembersFoundException;
import com.yms.projectservice.entity.ProjectStatus;
import com.yms.projectservice.service.abstracts.MemberClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private MemberClientService memberClient;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectCreateRequest projectCreateRequest;
    private Project project;
    private ProjectResponse projectResponse;
    private List<UserResponse> userResponses;

    @BeforeEach
    void setUp() {
        projectCreateRequest = ProjectCreateRequest.builder()
                .title("Test Project")
                .description("This is a test project.")
                .teamMemberIds(List.of(1, 2))
                .departmentName("IT")
                .projectStatus("In Progress")
                .build();

        project = Project.builder()
                .id(1)
                .title("Test Project")
                .description("This is a test project.")
                .teamMemberIds(List.of(1, 2, 3))
                .isDeleted(false)
                .departmentName("IT")
                .status(ProjectStatus.IN_PROGRESS)
                .build();

        projectResponse = ProjectResponse.builder()
                .id(1L)
                .title("Test Project")
                .description("This is a test project.")
                .departmentName("IT")
                .projectStatus("In Progress")
                .build();

        userResponses = List.of(
                UserResponse.builder()
                        .id(1)
                        .firstname("Yusuf")
                        .lastname("Solmaz")
                        .email("Yusuf.Solmaz@gmail.com")
                        .authorities(List.of("USER"))
                        .fullName("Yusuf Solmaz")
                        .build(),
                UserResponse.builder()
                        .id(2)
                        .firstname("Ali")
                        .lastname("Smith")
                        .email("Ali.smith@gmail.com")
                        .authorities(List.of("USER"))
                        .fullName("Ali Smith")
                        .build(),
                UserResponse.builder()
                        .id(3)
                        .firstname("Yusuf")
                        .lastname("Solmaz")
                        .email("yusuf@gmail.com")
                        .authorities(List.of("USER"))
                        .fullName("Yusuf Solmaz")
                        .build()
        );
    }

    @Test
    void testSave_ShouldSaveAndReturnProjectDto() {
        when(projectMapper.toProject(any(ProjectCreateRequest.class))).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toProjectResponse(any(Project.class))).thenReturn(projectResponse);
        when(memberClient.findUsersByIds(any(), any())).thenReturn(userResponses);

        ProjectResponse result = projectService.save(projectCreateRequest, "dummyToken");

        assertNotNull(result);
        assertEquals(projectResponse.title(), result.title());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testFindById_ShouldReturnProjectDto() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectMapper.toProjectResponse(any(Project.class))).thenReturn(projectResponse);

        ProjectResponse result = projectService.findById(1);

        assertNotNull(result);
        assertEquals(projectResponse.title(), result.title());
        verify(projectRepository).findById(anyInt());
    }

    @Test
    void testFindById_ShouldThrowException_WhenProjectNotFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(1));
    }

    @Test
    void testDeleteById_ShouldDeleteProject() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        projectService.deleteById(1);

        assertTrue(project.isDeleted());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testDeleteById_ShouldThrowException_WhenProjectNotFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteById(1));
    }

    @Test
    void testFindAll_ShouldReturnPagedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Project> projectPage = new PageImpl<>(List.of(project));

        when(projectRepository.findAll(pageable)).thenReturn(projectPage);
        when(projectMapper.toProjectResponse(any(Project.class))).thenReturn(projectResponse);

        PagedResponse<ProjectResponse> result = projectService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(projectResponse.title(), result.getContent().getFirst().title());
        verify(projectRepository).findAll(pageable);
    }

    @Test
    void testGetAllActiveProjects_ShouldReturnPagedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Project> projectPage = new PageImpl<>(List.of(project));

        when(projectRepository.findAllActives(pageable)).thenReturn(projectPage);
        when(projectMapper.toProjectResponse(any(Project.class))).thenReturn(projectResponse);

        PagedResponse<ProjectResponse> result = projectService.getAllActiveProjects(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(projectResponse.title(), result.getContent().getFirst().title());
        verify(projectRepository).findAllActives(pageable);
    }

    @Test
    void testGetAllMembers_ShouldReturnUserResponses() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(memberClient.findUsersByIds(anyList(), anyString())).thenReturn(userResponses);

        List<UserResponse> result = projectService.getAllMembers(1, "dummyToken");

        assertNotNull(result);
        assertEquals(userResponses.size(), result.size());
        assertEquals(userResponses.getFirst().fullName(), result.getFirst().fullName());
        verify(projectRepository).findById(anyInt());
        verify(memberClient).findUsersByIds(anyList(), anyString());
    }

    @Test
    void testGetAllMembers_ShouldThrowException_WhenNoMembersFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(memberClient.findUsersByIds(anyList(), anyString())).thenThrow(NoMembersFoundException.class);

        assertThrows(NoMembersFoundException.class, () -> projectService.getAllMembers(1, "dummyToken"));
    }

    @Test
    void testFindByDepartmentName_ShouldReturnProjects() {
        when(projectRepository.findAllByDepartmentName(anyString())).thenReturn(List.of(project));
        when(projectMapper.toProjectResponse(any(Project.class))).thenReturn(projectResponse);

        List<ProjectResponse> result = projectService.findByDepartmentName("IT");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(projectResponse.title(), result.getFirst().title());
        verify(projectRepository).findAllByDepartmentName(anyString());
    }

    @Test
    void testFindByDepartmentName_ShouldThrowException_WhenNoProjectsFound() {
        when(projectRepository.findAllByDepartmentName(anyString())).thenReturn(List.of());

        assertThrows(ProjectNotFoundException.class, () -> projectService.findByDepartmentName("NonExistent"));
    }
}

