package com.yms.task_service.service;

import com.yms.task_service.dto.TaskDto;
import com.yms.task_service.dto.request.TaskRequest;
import com.yms.task_service.entity.Task;
import com.yms.task_service.entity.TaskStatus;
import com.yms.task_service.exception.ProjectNotFound;
import com.yms.task_service.exception.TaskNotFound;
import com.yms.task_service.mapper.TaskMapper;
import com.yms.task_service.repository.TaskRepository;
import com.yms.task_service.service.abstracts.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectServiceClient projectServiceClient;


    @Override
    public TaskDto save(TaskRequest taskRequest, String token) {
        Task task = taskMapper.toTask(taskRequest);

        if (task.getReason() == null || task.getReason().isEmpty()) {
            task.setReason("No reason provided.");
        }

        projectServiceClient.getProjectById(task.getProjectId(), token);

        List<Integer> projectMemberIds = projectServiceClient.getProjectMemberIds(task.getProjectId(), token);

        List<Integer> invalidAssignees = task.getAssigneeId()
                .stream()
                .filter(id -> !projectMemberIds.contains(id))
                .toList();

        if (!invalidAssignees.isEmpty()) {
            throw new IllegalArgumentException("The following assignees are not part of the project team: " + invalidAssignees);
        }

        return taskMapper.toTaskDto(
                taskRepository.save(task)
        );
    }

    @Override
    public TaskDto findById(Integer id) {
        return taskRepository.findById(id)
                .map(taskMapper::toTaskDto)
                .orElseThrow(() -> new TaskNotFound("Task with ID " + id + " not found!"));
    }

    @Override
    public TaskDto findByIdAndNotCancelled(Integer projectId) {
        return taskRepository.findByIdAndNotCancelled(projectId)
                .map(taskMapper::toTaskDto)
                .orElseThrow(() -> new TaskNotFound("No Task Found with Project ID: " + projectId ));
    }

    @Override
    public List<TaskDto> findAllByProjectId(Integer projectId) {
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);

        if (tasks.isEmpty()) {
            throw new ProjectNotFound("Project with ID " + projectId + " has no tasks.");
        }

        return tasks.stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelTask(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFound("Task with ID " + id + " not found!"));

        task.setStatus(TaskStatus.CANCELLED);
        task.setReason("Deleted.");

        taskRepository.save(task);
    }

    @Override
    public TaskDto updateTaskStatus(Integer taskId, TaskStatus newStatus, String reason) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFound("Task not found"));

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new RuntimeException("Completed tasks cannot be updated.");
        }

        if ((newStatus == TaskStatus.CANCELLED || newStatus == TaskStatus.BLOCKED) && (reason == null || reason.isEmpty())) {
            throw new RuntimeException("A reason must be provided for CANCELLED or BLOCKED status.");
        }

        validateStateTransition(task.getStatus(), newStatus);

        task.setStatus(newStatus);
        taskRepository.save(task);

        return taskMapper.toTaskDto(task);
    }

    private void validateStateTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        if (currentStatus == TaskStatus.COMPLETED) {
            throw new RuntimeException("Cannot change status of a completed task.");
        }

        if (newStatus == TaskStatus.BLOCKED && currentStatus != TaskStatus.IN_ANALYSIS && currentStatus != TaskStatus.IN_PROGRESS) {
            throw new RuntimeException("Tasks can only be blocked if they are in IN_ANALYSIS or IN_PROGRESS.");
        }

        List<TaskStatus> allowedTransitions = switch (currentStatus) {
            case BACKLOG -> List.of(TaskStatus.IN_ANALYSIS);
            case IN_ANALYSIS -> List.of(TaskStatus.IN_PROGRESS, TaskStatus.BLOCKED, TaskStatus.CANCELLED);
            case IN_PROGRESS -> List.of(TaskStatus.COMPLETED, TaskStatus.BLOCKED, TaskStatus.CANCELLED);
            case BLOCKED -> List.of(TaskStatus.IN_ANALYSIS, TaskStatus.IN_PROGRESS);
            case CANCELLED, COMPLETED -> List.of(); // Geçiş yapılamaz
        };

        if (!allowedTransitions.contains(newStatus)) {
            throw new RuntimeException("Invalid state transition from " + currentStatus + " to " + newStatus);
        }
    }
}
