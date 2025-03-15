package com.yms.task_service.repository;

import com.yms.task_service.dto.TaskDto;
import com.yms.task_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findAllByProjectId(Integer projectId);
}
