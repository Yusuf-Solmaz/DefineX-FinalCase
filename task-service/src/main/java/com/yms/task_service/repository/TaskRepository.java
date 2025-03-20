package com.yms.task_service.repository;

import com.yms.task_service.dto.TaskDto;
import com.yms.task_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findAllByProjectId(Integer projectId);

    @Query("SELECT t FROM Task t WHERE t.projectId = :projectId AND t.status <> 'CANCELLED'")
    Optional<Task> findByIdAndNotCancelled(@Param("projectId") Integer projectId);
}
