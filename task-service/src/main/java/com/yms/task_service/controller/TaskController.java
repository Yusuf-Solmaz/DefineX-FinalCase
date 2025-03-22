package com.yms.task_service.controller;

import com.yms.task_service.dto.response.TaskResponse;
import com.yms.task_service.dto.request.TaskStatusUpdateRequest;
import com.yms.task_service.dto.response.UserResponse;
import com.yms.task_service.service.abstracts.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/tasks/open")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Integer taskId,
            @RequestBody @Valid TaskStatusUpdateRequest request) {

        TaskResponse updatedTask = taskService.updateTaskStatus(taskId, request.status(), request.reason());
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) {
        TaskResponse task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<List<UserResponse>> getProjectMember(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.getAllMembers(id,token));
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
}
