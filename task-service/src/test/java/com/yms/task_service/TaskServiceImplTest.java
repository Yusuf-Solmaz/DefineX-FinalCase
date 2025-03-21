package com.yms.task_service;

import com.yms.task_service.dto.request.TaskCreateRequest;
import com.yms.task_service.dto.response.ProjectResponse;
import com.yms.task_service.dto.response.TaskResponse;
import com.yms.task_service.entity.Task;
import com.yms.task_service.entity.TaskPriority;
import com.yms.task_service.entity.TaskStatus;
import com.yms.task_service.exception.TaskNotFoundException;
import com.yms.task_service.mapper.TaskMapper;
import com.yms.task_service.repository.TaskRepository;
import com.yms.task_service.service.ProjectServiceClient;
import com.yms.task_service.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private TaskMapper taskMapper;

	@Mock
	private ProjectServiceClient projectServiceClient;

	@InjectMocks
	private TaskServiceImpl taskService;

	private TaskCreateRequest validTaskCreateRequest;

	@BeforeEach
	void setUp() {

		validTaskCreateRequest = TaskCreateRequest.builder()
				.title("Task Title")
				.description("Task Description")
				.projectId(1)
				.assigneeId(Arrays.asList(1, 2))
				.priority("HIGH")
				.status("IN_PROGRESS")
				.reason("Initial Task")
				.build();
	}

	@Test
	void save_ShouldReturnTaskDto_WhenTaskIsValid() {

		Task task = Task.builder()
				.id(1)
				.title("Task Title")
				.description("Task Description")
				.projectId(1)
				.assigneeId(Arrays.asList(1, 2))
				.priority(TaskPriority.HIGH)
				.status(TaskStatus.IN_PROGRESS)
				.reason("Initial Task")
				.build();

		TaskResponse taskResponse = TaskResponse.builder()
				.id(1)
				.title("Task Title")
				.description("Task Description")
				.priority("HIGH")
				.status("IN_PROGRESS")
				.reason("Initial Task")
				.build();

		when(taskMapper.toTask(validTaskCreateRequest)).thenReturn(task);
		when(taskRepository.save(task)).thenReturn(task);
		when(taskMapper.toTaskResponse(task)).thenReturn(taskResponse);
		when(projectServiceClient.getProjectById(anyInt(), anyString())).thenReturn(new ProjectResponse(1L, "Project", "Description", "Development", "Active",false));
		when(projectServiceClient.getProjectMemberIds(anyInt(), anyString())).thenReturn(Arrays.asList(1, 2));

		// Act
		TaskResponse savedTask = taskService.save(validTaskCreateRequest, "valid-token");

		// Assert
		assertNotNull(savedTask);
		assertEquals(savedTask.id(), taskResponse.id());
		verify(taskRepository, times(1)).save(task);
	}


	@Test
	void findById_ShouldReturnTaskDto_WhenTaskExists() {
		// Arrange
		Task task = Task.builder()
				.id(1)
				.title("Task Title")
				.description("Task Description")
				.projectId(1)
				.assigneeId(Arrays.asList(1, 2))
				.priority(TaskPriority.HIGH)
				.status(TaskStatus.IN_PROGRESS)
				.reason("Initial Task")
				.build();

		TaskResponse taskResponse = TaskResponse.builder()
				.id(1)
				.title("Task Title")
				.description("Task Description")
				.priority("HIGH")
				.status("IN_PROGRESS")
				.reason("Initial Task")
				.build();

		when(taskRepository.findById(1)).thenReturn(Optional.of(task));
		when(taskMapper.toTaskResponse(task)).thenReturn(taskResponse);

		// Act
		TaskResponse foundTask = taskService.findById(1);

		// Assert
		assertNotNull(foundTask);
		assertEquals(foundTask.id(), taskResponse.id());
	}

	@Test
	void findById_ShouldThrowTaskNotFound_WhenTaskDoesNotExist() {
		// Arrange
		when(taskRepository.findById(1)).thenReturn(Optional.empty());

		// Act & Assert
		TaskNotFoundException thrown = assertThrows(TaskNotFoundException.class, () -> taskService.findById(1));
		assertEquals("Task with ID 1 not found!", thrown.getMessage());
	}

	@Test
	void cancelTask_ShouldUpdateTaskStatusToCancelled() {
		// Arrange
		Task task = Task.builder()
				.id(1)
				.status(TaskStatus.IN_PROGRESS)
				.reason("Initial Task")
				.build();

		when(taskRepository.findById(1)).thenReturn(Optional.of(task));
		when(taskRepository.save(task)).thenReturn(task);

		// Act
		taskService.cancelTask(1);

		// Assert
		assertEquals(TaskStatus.CANCELLED, task.getStatus());
		assertEquals("Deleted.", task.getReason());
		verify(taskRepository, times(1)).save(task);
	}

	@Test
	void cancelTask_ShouldThrowTaskNotFound_WhenTaskDoesNotExist() {
		// Arrange
		when(taskRepository.findById(1)).thenReturn(Optional.empty());

		// Act & Assert
		TaskNotFoundException thrown = assertThrows(TaskNotFoundException.class, () -> taskService.cancelTask(1));
		assertEquals("Task with ID 1 not found!", thrown.getMessage());
	}

	@Test
	void updateTaskStatus_ShouldThrowRuntimeException_WhenTaskIsCompleted() {
		// Arrange
		Task task = Task.builder()
				.id(1)
				.status(TaskStatus.COMPLETED)
				.build();

		when(taskRepository.findById(1)).thenReturn(Optional.of(task));

		// Act & Assert
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> taskService.updateTaskStatus(1, TaskStatus.IN_PROGRESS, "Restarting task"));
		assertEquals("Completed tasks cannot be updated.", thrown.getMessage());
	}
}

