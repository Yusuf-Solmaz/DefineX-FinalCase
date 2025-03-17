package com.yms.task_service.client;

import com.yms.task_service.dto.ProjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name= "project-service",url = "${application.config.projects-url}")
public interface ProjectClient {

    @GetMapping("/{id}")
    ProjectResponse findProjectById(@PathVariable Integer id, @RequestHeader("Authorization") String token);

    @GetMapping("/member-ids/{id}")
    List<Integer> getProjectMemberIds(@PathVariable Integer id, @RequestHeader("Authorization") String token);
}
