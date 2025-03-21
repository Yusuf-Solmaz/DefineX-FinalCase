package com.yms.task_service.mapper;

import com.yms.task_service.dto.request.TaskCreateRequest;
import com.yms.task_service.dto.response.TaskResponse;
import com.yms.task_service.entity.Task;
import com.yms.task_service.entity.TaskPriority;
import com.yms.task_service.entity.TaskStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toTaskResponse(Task task);

    Task toTask(TaskCreateRequest taskCreateRequest);

    default String mapTaskStatusToString(TaskStatus status) {
        return status != null ? status.getTaskStatus() : null;
    }

    default String mapTaskPriorityToString(TaskPriority status) {
        return status != null ? status.getTaskPriority() : null;
    }

    default TaskStatus mapStringToTaskStatus(String status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.name().equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        return null;
    }

    default TaskPriority mapStringToTaskPriority(String status) {
        for (TaskPriority taskPriority : TaskPriority.values()) {
            if (taskPriority.name().equalsIgnoreCase(status)) {
                return taskPriority;
            }
        }
        return null;
    }
}
