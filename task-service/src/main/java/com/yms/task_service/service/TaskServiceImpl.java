package com.yms.task_service.service;

import com.yms.task_service.dto.response.ProjectResponse;
import com.yms.task_service.dto.response.TaskResponse;
import com.yms.task_service.dto.response.UserResponse;
import com.yms.task_service.dto.request.TaskCreateRequest;
import com.yms.task_service.dto.request.TaskUpdateRequest;
import com.yms.task_service.entity.Task;
import com.yms.task_service.entity.TaskPriority;
import com.yms.task_service.entity.TaskStatus;
import com.yms.task_service.exception.*;
import com.yms.task_service.exception.exception_response.ErrorMessages;
import com.yms.task_service.mapper.TaskMapper;
import com.yms.task_service.repository.TaskRepository;
import com.yms.task_service.service.abstracts.MemberClientService;
import com.yms.task_service.service.abstracts.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static com.yms.task_service.exception.exception_response.ErrorMessages.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectServiceClient projectServiceClient;
    private final MemberClientService clientService;


    @Override
    public TaskResponse save(TaskCreateRequest taskCreateRequest, String token) {
        Task task = taskMapper.toTask(taskCreateRequest);

        if (task.getReason() == null || task.getReason().isEmpty()) {
            task.setReason(TASK_REASON_NOT_PROVIDED);
        }

        ProjectResponse projectResponse = projectServiceClient.getProjectById(task.getProjectId(), token);

        if (projectResponse.isDeleted()){
            throw new ProjectNotFoundException(PROJECT_DELETED);
        }

        List<Integer> projectMemberIds = projectServiceClient.getProjectMemberIds(task.getProjectId(), token);

        List<Integer> invalidAssignees = task.getAssigneeId()
                .stream()
                .filter(id -> !projectMemberIds.contains(id))
                .toList();

        if (!invalidAssignees.isEmpty()) {
            throw new IllegalArgumentException(String.format(INVALID_ASSIGNEE, invalidAssignees));
        }

        return taskMapper.toTaskResponse(
                taskRepository.save(task)
        );
    }

    @Override
    public TaskResponse findById(Integer id) {
        return taskRepository.findById(id)
                .map(taskMapper::toTaskResponse)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND, id)));
    }

    @Override
    public TaskResponse findByIdAndNotCancelled(Integer projectId) {
        return taskRepository.findByIdAndNotCancelled(projectId)
                .map(taskMapper::toTaskResponse)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND, projectId) ));
    }

    @Override
    public List<TaskResponse> findAllByProjectId(Integer projectId) {
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);

        if (tasks.isEmpty()) {
            throw new ProjectNotFoundException(String.format(PROJECT_TASKS_NOT_FOUND, projectId));
        }

        return tasks.stream()
                .map(taskMapper::toTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelTask(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND, id)));

        task.setStatus(TaskStatus.CANCELLED);
        task.setReason("Deleted.");

        taskRepository.save(task);
    }

    @Override
    public TaskResponse updateTaskStatus(Integer taskId, TaskStatus newStatus, String reason) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND, taskId)));

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new RuntimeException(UPDATE_TASK_COMPLETED);
        }

        if ((newStatus == TaskStatus.CANCELLED || newStatus == TaskStatus.BLOCKED) && (reason == null || reason.isEmpty())) {
            throw new RuntimeException(TASK_CANCELLED_REASON_REQUIRED);
        }

        validateStateTransition(task.getStatus(), newStatus);

        task.setStatus(newStatus);
        taskRepository.save(task);

        return taskMapper.toTaskResponse(task);
    }

    @Override
    public List<UserResponse> getAllMembers(Integer taskId, String token) {
        List<Integer> memberIds = taskRepository.findById(taskId)
                .stream()
                .flatMap(task -> task.getAssigneeId().stream())
                .distinct()
                .toList();


        if (memberIds.isEmpty()) {
            throw new NoMembersFoundException(NO_MEMBERS_FOUND);
        }

        return clientService.findUsersByIds(memberIds, token);
    }

    public TaskResponse updateTask(Integer taskId, TaskUpdateRequest request,String token) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND, taskId)));

        mergeTaskDetails(task, request,token);

        taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }

    private void mergeTaskDetails(Task task, TaskUpdateRequest request, String token) {
        if (request.title() != null && !request.title().isEmpty()) {
            task.setTitle(request.title());
        }

        if (request.description() != null && !request.description().isEmpty()) {
            task.setDescription(request.description());
        }

        if (request.priority() != null) {
            TaskPriority newPriority = taskMapper.mapStringToTaskPriority(request.priority());
            if (newPriority != null) {
                task.setPriority(newPriority);
            } else {
                throw new InvalidPriorityException(String.format(INVALID_PRIORITY,request.priority()));
            }
        }

        if (request.assigneeId() != null && !request.assigneeId().isEmpty()) {
            List<Integer> projectMemberIds = projectServiceClient.getProjectMemberIds(task.getProjectId(), token);
            List<Integer> invalidAssignees = request.assigneeId()
                    .stream()
                    .filter(id -> !projectMemberIds.contains(id))
                    .toList();

            if (!invalidAssignees.isEmpty()) {
                throw new InvalidAssigneesException(INVALID_ASSIGNEES + invalidAssignees);
            }

            task.setAssigneeId(request.assigneeId());
        }

        if (request.reason() != null && !request.reason().isEmpty()) {
            task.setReason(request.reason());
        }
    }


    private void validateStateTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        if (newStatus == TaskStatus.BLOCKED && currentStatus != TaskStatus.IN_ANALYSIS && currentStatus != TaskStatus.IN_PROGRESS) {
            throw new RuntimeException(ErrorMessages.TASK_BLOCKED_INVALID_STATE);
        }

        List<TaskStatus> allowedTransitions = switch (currentStatus) {
            case BACKLOG -> List.of(TaskStatus.IN_ANALYSIS);
            case IN_ANALYSIS -> List.of(TaskStatus.IN_PROGRESS, TaskStatus.BLOCKED, TaskStatus.CANCELLED);
            case IN_PROGRESS -> List.of(TaskStatus.COMPLETED, TaskStatus.BLOCKED, TaskStatus.CANCELLED);
            case BLOCKED -> List.of(TaskStatus.IN_ANALYSIS, TaskStatus.IN_PROGRESS);
            case CANCELLED -> List.of();
            case COMPLETED -> throw new RuntimeException(ErrorMessages.CHANGE_TASK_COMPLETED);
        };

        if (!allowedTransitions.contains(newStatus)) {
            throw new RuntimeException(String.format(TASK_STATUS_TRANSITION_INVALID,currentStatus,newStatus));
        }
    }

}
