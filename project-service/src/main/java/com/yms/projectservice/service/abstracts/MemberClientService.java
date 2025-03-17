package com.yms.projectservice.service.abstracts;

import com.yms.projectservice.dto.UserResponse;

import java.util.List;

public interface MemberClientService {
    List<UserResponse> findUsersByIds(List<Integer> ids, String token);
}
