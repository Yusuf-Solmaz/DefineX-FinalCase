package com.yms.comment_service.service;

import com.yms.comment_service.client.TaskClient;
import com.yms.comment_service.dto.response.TaskResponse;
import com.yms.comment_service.exception.TaskServiceUnavailableException;
import com.yms.comment_service.exception.TaskNotFoundException;
import com.yms.comment_service.exception.exception_response.ErrorMessages;
import com.yms.comment_service.service.abstracts.TaskClientService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskClientServiceImpl implements TaskClientService {

    private final TaskClient taskClient;

    @Override
    public TaskResponse findTaskById(Integer id, String token) {
        try{
            return taskClient.findTaskById(id, token);
        }catch (FeignException.NotFound e){
            throw new TaskNotFoundException(String.format(ErrorMessages.TASK_NOT_FOUND,id));
        }catch (Exception e){
            throw new TaskServiceUnavailableException(ErrorMessages.TASK_SERVICE_UNAVAILABLE);
        }
    }
}
