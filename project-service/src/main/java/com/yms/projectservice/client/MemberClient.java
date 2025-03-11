package com.yms.projectservice.client;

import com.yms.projectservice.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "member-service",url = "${application.config.members-url}")
public interface MemberClient {

    @GetMapping("/batch")
    List<MemberResponse> findUsersByIds(@RequestParam List<Long> ids);
}
