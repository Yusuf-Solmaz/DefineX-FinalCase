package com.yms.task_service.service;

import com.yms.task_service.client.ProjectClient;
import com.yms.task_service.dto.ProjectResponse;
import com.yms.task_service.exception.ProjectNotFound;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceClient {

    private final ProjectClient projectClient;

    public ProjectResponse getProjectById(Integer projectId, String token) {
        try {
            return projectClient.findProjectById(projectId, token);
        } catch (FeignException.NotFound e) {
            throw new ProjectNotFound("Project with ID " + projectId + " not found.");
        } catch (Exception e) {
            throw new RuntimeException("Project service is unavailable. Please try again later.");
        }
    }

    public List<Integer> getProjectMemberIds(Integer projectId, String token) {
        try {
            return projectClient.getProjectMemberIds(projectId, token);
        } catch (FeignException.NotFound e) {
            throw new ProjectNotFound("Project with ID " + projectId + " not found.");
        } catch (Exception e) {
            throw new RuntimeException("Project service is unavailable. Please try again later.");
        }
    }
}

