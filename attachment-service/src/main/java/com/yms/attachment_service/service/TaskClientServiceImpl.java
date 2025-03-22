package com.yms.attachment_service.service;

import com.yms.attachment_service.client.TaskClient;
import com.yms.attachment_service.exceptions.ProjectServiceUnavailableException;
import com.yms.attachment_service.exceptions.TaskNotFoundException;
import com.yms.attachment_service.exceptions.exception_response.ErrorMessages;
import com.yms.attachment_service.service.abstracts.TaskClientService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskClientServiceImpl implements TaskClientService {

    private final TaskClient taskClient;

    @Override
    public void findTaskById(Integer id, String token) {
        try{
            taskClient.findTaskById(id, token);
        }catch (FeignException.NotFound e){
            throw new TaskNotFoundException(String.format(ErrorMessages.TASK_NOT_FOUND, id));
        }catch (Exception e){
            throw new ProjectServiceUnavailableException(ErrorMessages.PROJECT_SERVICE_UNAVAILABLE);
        }
    }
}
