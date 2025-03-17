package com.yms.projectservice.service;

import com.yms.projectservice.client.MemberClient;
import com.yms.projectservice.dto.UserResponse;
import com.yms.projectservice.exception.NoMembersFoundException;
import com.yms.projectservice.service.abstracts.MemberClientService;
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
