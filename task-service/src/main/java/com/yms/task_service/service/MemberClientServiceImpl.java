package com.yms.task_service.service;

import com.yms.task_service.client.MemberClient;
import com.yms.task_service.dto.response.UserResponse;
import com.yms.task_service.exception.NoMembersFoundException;
import com.yms.task_service.exception.UserServiceUnavailableException;
import com.yms.task_service.exception.exception_response.ErrorMessages;
import com.yms.task_service.service.abstracts.MemberClientService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberClientServiceImpl implements MemberClientService {

    private final MemberClient memberClient;

    @Override
    public List<UserResponse> findUsersByIds(List<Integer> ids, String token) {
        try{
            return memberClient.findUsersByIds(ids, token);
        }catch (FeignException.NotFound e){
            throw new NoMembersFoundException(ErrorMessages.NO_MEMBERS_FOUND);
        }catch (Exception e){
            throw new UserServiceUnavailableException(ErrorMessages.USER_SERVICE_UNAVAILABLE);
        }
    }
}
