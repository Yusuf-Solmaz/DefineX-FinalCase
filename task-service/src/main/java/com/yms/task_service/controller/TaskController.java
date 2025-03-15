package com.yms.task_service.controller;

import com.yms.task_service.dto.TaskDto;
import com.yms.task_service.dto.UpdateTaskStatusRequest;
import com.yms.task_service.entity.Task;
import com.yms.task_service.service.abstracts.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDto> createTask(@RequestBody Task task) {
        return ResponseEntity.created(
                URI.create("/api/v1/tasks"+"/"+task.getId())
        ).body(taskService.save(task));
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Integer id) {
        TaskDto task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskDto>> getTasksByProjectId(@PathVariable Integer projectId) {
        List<TaskDto> tasks = taskService.findAllByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }


    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Integer taskId,
            @RequestBody UpdateTaskStatusRequest request) {

        Task updatedTask = taskService.updateTaskStatus(taskId, request.status(), request.reason());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTask(@PathVariable Integer id) {
        taskService.cancelTask(id);
        return ResponseEntity.noContent().build();
    }
}
