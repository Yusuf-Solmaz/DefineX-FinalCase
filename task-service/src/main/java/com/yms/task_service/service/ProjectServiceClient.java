package com.yms.task_service.service;

import com.yms.task_service.client.ProjectClient;
import com.yms.task_service.dto.response.ProjectResponse;
import com.yms.task_service.exception.ProjectNotFoundException;
import com.yms.task_service.exception.ProjectServiceUnavailableException;
import com.yms.task_service.exception.exception_response.ErrorMessages;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yms.task_service.exception.exception_response.ErrorMessages.PROJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProjectServiceClient {

    private final ProjectClient projectClient;

    public ProjectResponse getProjectById(Integer projectId, String token) {
        try {
            return projectClient.findProjectById(projectId, token);
        } catch (FeignException.NotFound e) {
            throw new ProjectNotFoundException(String.format(PROJECT_NOT_FOUND, projectId));
        } catch (Exception e) {
            throw new ProjectServiceUnavailableException(String.format(ErrorMessages.PROJECT_SERVICE_UNAVAILABLE,e.getMessage()));
        }
    }

    public List<Integer> getProjectMemberIds(Integer projectId, String token) {
        try {
            return projectClient.getProjectMemberIds(projectId, token);
        } catch (FeignException.NotFound e) {
            throw new ProjectNotFoundException(String.format(PROJECT_NOT_FOUND, projectId));
        } catch (Exception e) {
            throw new ProjectServiceUnavailableException(String.format(ErrorMessages.PROJECT_SERVICE_UNAVAILABLE,e.getMessage()));
        }
    }
}

