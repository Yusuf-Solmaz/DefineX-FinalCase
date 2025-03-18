package com.yms.attachment_service.service;

import com.yms.attachment_service.client.TaskClient;
import com.yms.attachment_service.exceptions.TaskNotFoundException;
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
            throw new TaskNotFoundException("Task with ID " + id + " not found!");
        }catch (Exception e){
            throw new RuntimeException("Project service is unavailable. Please try again later.");
        }
    }
}
