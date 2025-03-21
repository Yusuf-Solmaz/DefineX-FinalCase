package com.yms.task_service.service.abstracts;


import com.yms.task_service.dto.response.UserResponse;

import java.util.List;

public interface MemberClientService {
    List<UserResponse> findUsersByIds(List<Integer> ids, String token);
}
