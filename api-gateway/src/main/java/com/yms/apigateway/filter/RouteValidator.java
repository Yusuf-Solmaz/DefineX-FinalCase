package com.yms.apigateway.filter;

import com.yms.apigateway.util.Roles;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Predicate;
import java.util.List;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/**"
    );

    public static final Map<String, List<String>> authorizationRequiredEndpoints = Map.of(
            "/api/v1/projects", List.of(Roles.MANAGER, Roles.LEADER),

            "/api/v1/tasks", List.of(Roles.MANAGER, Roles.LEADER),
            "/api/v1/tasks/{taskId}/status", List.of(Roles.MANAGER, Roles.LEADER,Roles.MEMBER),
            "/api/v1/tasks/project/{projectId}", List.of(Roles.MANAGER, Roles.LEADER,Roles.MEMBER),

            "/api/v1/comments", List.of(Roles.MANAGER, Roles.LEADER,Roles.MEMBER),
            "/api/v1/attachments", List.of(Roles.MANAGER, Roles.LEADER,Roles.MEMBER)
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> authorizationRequiredEndpoints.keySet()
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().startsWith(uri));

    public Predicate<ServerHttpRequest> isOpenApi =
            request -> openApiEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().startsWith(uri));
}