package com.yms.task_service.client;


import com.yms.task_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "member-service",url = "${application.config.members-url}")
public interface MemberClient {

    @GetMapping("/batch")
    List<UserResponse> findUsersByIds(@RequestParam List<Integer> ids, @RequestHeader("Authorization") String token);
}
