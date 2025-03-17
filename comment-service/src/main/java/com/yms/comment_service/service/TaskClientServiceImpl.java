package com.yms.comment_service.service;

import com.yms.comment_service.client.TaskClient;
import com.yms.comment_service.dto.TaskResponse;
import com.yms.comment_service.exception.TaskNotFoundException;
import com.yms.comment_service.service.abstracts.TaskClientService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.Task;
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
            throw new TaskNotFoundException("Task with ID " + id + " not found!");
        }catch (Exception e){
            throw new RuntimeException("Project service is unavailable. Please try again later.");
        }
    }
}
