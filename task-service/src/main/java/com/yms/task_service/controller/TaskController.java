package com.yms.task_service.controller;

import com.yms.task_service.dto.TaskResponse;
import com.yms.task_service.dto.UpdateTaskStatusRequest;
import com.yms.task_service.dto.UserResponse;
import com.yms.task_service.dto.request.TaskRequest;
import com.yms.task_service.dto.request.TaskUpdateRequest;
import com.yms.task_service.service.abstracts.TaskService;
import jakarta.validation.Valid;
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
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody @Valid TaskRequest taskRequest,
            @RequestHeader("Authorization") String token) {
        TaskResponse savedTask = taskService.save(taskRequest, token);
        return ResponseEntity.created(
                URI.create("/api/v1/tasks" + "/" + savedTask.id())
        ).body(savedTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Integer taskId,
            @RequestBody TaskUpdateRequest request,
            @RequestHeader("Authorization") String token) {

        TaskResponse updatedTask = taskService.updateTask(taskId, request,token);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) {
        TaskResponse task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/actives/{projectId}")
    public ResponseEntity<TaskResponse> getTaskByIdAndNotCancelled(@PathVariable Integer projectId) {
        TaskResponse task = taskService.findByIdAndNotCancelled(projectId);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProjectId(@PathVariable Integer projectId) {
        List<TaskResponse> tasks = taskService.findAllByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }


    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Integer taskId,
            @RequestBody UpdateTaskStatusRequest request) {

        TaskResponse updatedTask = taskService.updateTaskStatus(taskId, request.status(), request.reason());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTask(@PathVariable Integer id) {
        taskService.cancelTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<List<UserResponse>> getProjectMember(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.getAllMembers(id,token));
    }
}
