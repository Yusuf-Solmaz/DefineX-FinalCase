package com.yms.task_service.controller;


import com.yms.task_service.dto.request.TaskCreateRequest;
import com.yms.task_service.dto.response.TaskResponse;
import com.yms.task_service.dto.request.TaskStatusUpdateRequest;
import com.yms.task_service.dto.response.UserResponse;
import com.yms.task_service.dto.request.TaskUpdateRequest;
import com.yms.task_service.service.abstracts.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("api/v1/tasks/admin")
@RequiredArgsConstructor
public class TaskAdminController {

    private final TaskService taskService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody @Valid TaskCreateRequest taskCreateRequest,
            @RequestHeader("Authorization") String token) {
        TaskResponse savedTask = taskService.save(taskCreateRequest, token);
        return ResponseEntity.created(
                URI.create("/api/v1/tasks" + "/" + savedTask.id())
        ).body(savedTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Integer taskId,
            @RequestBody @Valid TaskUpdateRequest request,
            @RequestHeader("Authorization") String token) {

        TaskResponse updatedTask = taskService.updateTask(taskId, request,token);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTask(@PathVariable Integer id) {
        taskService.cancelTask(id);
        return ResponseEntity.noContent().build();
    }

}
