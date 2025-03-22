package com.yms.task_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yms.task_service.controller.TaskAdminController;
import com.yms.task_service.dto.request.TaskCreateRequest;
import com.yms.task_service.dto.request.TaskUpdateRequest;
import com.yms.task_service.dto.response.TaskResponse;
import com.yms.task_service.exception.GlobalExceptionHandler;
import com.yms.task_service.service.abstracts.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskAdminController taskController;

    private TaskUpdateRequest validTaskUpdateRequest;
    private ObjectMapper objectMapper;

    private TaskCreateRequest taskCreateRequest;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();


        validTaskUpdateRequest = new TaskUpdateRequest(
                "Updated Task Title",
                "Updated Task Description with more than 10 characters",
                "High",
                List.of(1, 2),
                "Reason for update"
        );

        taskCreateRequest = TaskCreateRequest.builder()
                .title("Test Task")
                .description("Test task description")
                .projectId(1)
                .assigneeId(Arrays.asList(1, 2))
                .priority("HIGH")
                .status("IN_PROGRESS")
                .reason("Initial task")
                .build();

        taskResponse = TaskResponse.builder()
                .id(1)
                .title("Test Task")
                .description("Test task description")
                .id(1)
                .priority("HIGH")
                .status("IN_PROGRESS")
                .reason("Initial task")
                .build();
    }

    @Test
    void createTask_ShouldReturnCreated() throws Exception {
        when(taskService.save(any(TaskCreateRequest.class), anyString())).thenReturn(taskResponse);

        mockMvc.perform(post("/api/v1/tasks/admin")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(taskCreateRequest))
                        .header("Authorization", "Bearer some_token"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/tasks/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        TaskResponse taskResponse = new TaskResponse(1, "Updated Task Title", "Updated Task Description with more than 10 characters", "High","IN_PROGRESS" , "Reason for update");

        when(taskService.updateTask(any(Integer.class), any(TaskUpdateRequest.class), any(String.class)))
                .thenReturn(taskResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/admin/1")
                        .header("Authorization", "Bearer your_token_here")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validTaskUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskResponse.id()))
                .andExpect(jsonPath("$.title").value(taskResponse.title()))
                .andExpect(jsonPath("$.description").value(taskResponse.description()))
                .andExpect(jsonPath("$.priority").value(taskResponse.priority()))
                .andExpect(jsonPath("$.reason").value(taskResponse.reason()));
    }

    @Test
    void updateTask_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        TaskUpdateRequest invalidRequest = new TaskUpdateRequest(
                "T",
                "Short",
                "Low",
                List.of(1),
                "Reason"
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/admin/1")
                        .header("Authorization", "Bearer your_token_here")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelTask_ShouldReturnNoContent() throws Exception {
        doNothing().when(taskService).cancelTask(1);

        mockMvc.perform(delete("/api/v1/tasks/admin/1"))
                .andExpect(status().isNoContent());
    }
}
