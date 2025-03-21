package com.yms.task_service.service;

import com.yms.task_service.client.MemberClient;
import com.yms.task_service.dto.UserResponse;
import com.yms.task_service.exception.NoMembersFoundException;
import com.yms.task_service.service.abstracts.MemberClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberClientServiceImpl implements MemberClientService {

    private final MemberClient memberClient;

    @Override
    public List<UserResponse> findUsersByIds(List<Integer> ids, String token) {
        if (ids == null || ids.isEmpty()) {
            throw new NoMembersFoundException("User ID list is empty.");
        }
        return memberClient.findUsersByIds(ids, token);
    }
}
