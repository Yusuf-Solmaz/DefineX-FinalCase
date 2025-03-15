package com.yms.task_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    @GetMapping
    ResponseEntity<String> getHello(){
        return ResponseEntity.ok("Hello World");
    }
}
