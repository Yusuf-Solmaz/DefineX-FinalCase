package com.yms.task_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yms.task_service.controller.TaskController;
import com.yms.task_service.dto.response.TaskResponse;
import com.yms.task_service.dto.request.TaskStatusUpdateRequest;
import com.yms.task_service.dto.request.TaskCreateRequest;
import com.yms.task_service.entity.TaskStatus;
import com.yms.task_service.exception.GlobalExceptionHandler;
import com.yms.task_service.service.abstracts.TaskService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

class TaskControllerTest {


    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;


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
    void getTaskById_ShouldReturnTask() throws Exception {
        when(taskService.findById(1)).thenReturn(taskResponse);

        mockMvc.perform(get("/api/v1/tasks/open/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void getTasksByProjectId_ShouldReturnTasks() throws Exception {
        List<TaskResponse> taskResponses = Collections.singletonList(taskResponse);
        when(taskService.findAllByProjectId(1)).thenReturn(taskResponses);

        mockMvc.perform(get("/api/v1/tasks/open/project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void updateTaskStatus_ShouldReturnUpdatedTask() throws Exception {
        TaskStatusUpdateRequest statusRequest = new TaskStatusUpdateRequest(TaskStatus.COMPLETED, "Completed successfully");

        when(taskService.updateTaskStatus(1, TaskStatus.COMPLETED, "Completed successfully")).thenReturn(taskResponse);

        mockMvc.perform(patch("/api/v1/tasks/open/1/status")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

}