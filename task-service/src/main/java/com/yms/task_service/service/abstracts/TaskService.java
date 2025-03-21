package com.yms.task_service.service.abstracts;

import com.yms.task_service.dto.TaskResponse;
import com.yms.task_service.dto.UserResponse;
import com.yms.task_service.dto.request.TaskRequest;
import com.yms.task_service.dto.request.TaskUpdateRequest;
import com.yms.task_service.entity.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskResponse save(TaskRequest task, String token);
    TaskResponse findById(Integer id);
    List<TaskResponse> findAllByProjectId(Integer projectId);
    void cancelTask(Integer id);

    TaskResponse updateTaskStatus(Integer taskId, TaskStatus status, String reason);
    TaskResponse findByIdAndNotCancelled(Integer id);
    TaskResponse updateTask(Integer taskId, TaskUpdateRequest request, String token);
    List<UserResponse> getAllMembers(Integer taskId, String token);
}
