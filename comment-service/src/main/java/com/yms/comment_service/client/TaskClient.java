package com.yms.comment_service.client;

import com.yms.comment_service.dto.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name= "task-service",url = "${application.config.tasks-url}")
public interface TaskClient {

    @GetMapping("/{id}")
    TaskResponse findTaskById(@PathVariable Integer id, @RequestHeader("Authorization") String token);
}
