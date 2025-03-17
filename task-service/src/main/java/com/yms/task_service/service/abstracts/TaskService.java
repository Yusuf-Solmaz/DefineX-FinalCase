package com.yms.task_service.service.abstracts;

import com.yms.task_service.dto.TaskDto;
import com.yms.task_service.entity.Task;
import com.yms.task_service.entity.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskDto save(Task task,String token);
    TaskDto findById(Integer id);
    List<TaskDto> findAllByProjectId(Integer projectId);
    void cancelTask(Integer id);

    Task updateTaskStatus(Integer taskId, TaskStatus status, String reason);
}
