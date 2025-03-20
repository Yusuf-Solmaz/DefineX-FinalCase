package com.yms.projectservice;

import com.yms.projectservice.controller.ProjectController;
import com.yms.projectservice.dto.PagedResponse;
import com.yms.projectservice.dto.ProjectRequest;
import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.dto.UserResponse;
import com.yms.projectservice.exception.GlobalExceptionHandler;
import com.yms.projectservice.service.abstracts.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProjectControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ProjectController controller;

    @Mock
    private ProjectService projectService;


    private ProjectDto projectDto;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        projectDto = ProjectDto.builder()
                .id(1L)
                .title("Test Project")
                .description("This is a test project.")
                .departmentName("IT")
                .projectStatus("In Progress")
                .build();
    }

    @Test
    void getAllProjects_ShouldReturnPagedResponse() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        PagedResponse<ProjectDto> pagedResponse = new PagedResponse<>(
                List.of(projectDto), 0, 10, 1L, 1, true
        );

        when(projectService.findAll(pageable)).thenReturn(pagedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/all")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("Test Project")))
                .andExpect(jsonPath("$.content[0].description", is("This is a test project.")))
                .andExpect(jsonPath("$.content[0].departmentName", is("IT")));
    }

    @Test
    void getProjectsByDepartment_ShouldReturnList() throws Exception {
        List<ProjectDto> projects = Collections.singletonList(projectDto);

        when(projectService.findByDepartmentName("IT")).thenReturn(projects);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/department/IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Test Project")))
                .andExpect(jsonPath("$[0].description", is("This is a test project.")))
                .andExpect(jsonPath("$[0].departmentName", is("IT")));
    }

    @Test
    void getProjectById_ShouldReturnProject() throws Exception {
        when(projectService.findById(1)).thenReturn(projectDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Project")))
                .andExpect(jsonPath("$.description", is("This is a test project.")))
                .andExpect(jsonPath("$.departmentName", is("IT")));
    }

    @Test
    void createProject_ShouldReturnCreated() throws Exception {
        String token = "Bearer token";
        when(projectService.save(ArgumentMatchers.any(ProjectRequest.class), ArgumentMatchers.anyString()))
                .thenReturn(projectDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content("{\"title\":\"Test Project\",\"description\":\"This is a test project.\",\"teamMemberIds\":[1,2],\"departmentName\":\"IT\",\"projectStatus\":\"In Progress\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/projects/1"))
                .andExpect(jsonPath("$.title", is("Test Project")))
                .andExpect(jsonPath("$.description", is("This is a test project.")))
                .andExpect(jsonPath("$.departmentName", is("IT")));
    }

    @Test
    void deleteProjectById_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/projects/1"))
                .andExpect(status().isNoContent());

        verify(projectService).deleteById(1);
    }

    @Test
    void getProjectMember_ShouldReturnListOfMembers() throws Exception {
        List<UserResponse> userResponses = Arrays.asList(
                UserResponse.builder().id(1).firstname("John").lastname("Doe").email("john.doe@example.com").authorities(List.of("USER")).fullName("John Doe").build(),
                UserResponse.builder().id(2).firstname("Jane").lastname("Smith").email("jane.smith@example.com").authorities(List.of("USER")).fullName("Jane Smith").build()
        );

        String token = "Bearer token";
        when(projectService.getAllMembers(1, token)).thenReturn(userResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/members/1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[1].fullName", is("Jane Smith")));
    }

    @Test
    void getProjectMemberIds_ShouldReturnListOfMemberIds() throws Exception {
        List<Integer> memberIds = List.of(1, 2, 3);
        when(projectService.getAllMembersId(1)).thenReturn(memberIds);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/member-ids/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is(1)))
                .andExpect(jsonPath("$[1]", is(2)))
                .andExpect(jsonPath("$[2]", is(3)));
    }
}

