package com.yms.projectservice.service;

import com.yms.projectservice.client.MemberClient;
import com.yms.projectservice.dto.response.UserResponse;
import com.yms.projectservice.exception.NoMembersFoundException;
import com.yms.projectservice.exception.UserServiceUnavailableException;
import com.yms.projectservice.exception.exception_response.ErrorMessages;
import com.yms.projectservice.service.abstracts.MemberClientService;
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
        }
        catch (RuntimeException e){
            throw new UserServiceUnavailableException(ErrorMessages.USER_SERVICE_UNAVAILABLE);
        }
    }
}
