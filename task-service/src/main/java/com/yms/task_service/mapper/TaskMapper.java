package com.yms.task_service.mapper;

import com.yms.task_service.dto.TaskDto;
import com.yms.task_service.entity.Task;
import com.yms.task_service.entity.TaskPriority;
import com.yms.task_service.entity.TaskStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toTaskDto(Task task);

    default String mapProjectStatusToString(TaskStatus status) {
        return status != null ? status.getTaskStatus() : null;
    }

    default String mapProjectStatusToString(TaskPriority status) {
        return status != null ? status.getTaskPriority() : null;
    }
}
