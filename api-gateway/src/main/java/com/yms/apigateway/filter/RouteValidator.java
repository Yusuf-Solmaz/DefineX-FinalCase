package com.yms.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/**",
            "/api/v1/search/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    );

    // Authorization gerektiren API endpoint'leri (bu endpoint'ler sadece geçerli bir token ile erişilebilir)
    public static final List<String> authorizationRequiredEndpoints = List.of(
            "/api/v1/projects",
            "/api/v1/user"
    );

    // isSecured predikatı, eğer istenen endpoint authorization gerektiriyorsa true döner
    public Predicate<ServerHttpRequest> isSecured =
            request -> authorizationRequiredEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().startsWith(uri)); // startsWith kullanarak daha hassas eşleşme

    // isOpenApi predikatı, eğer endpoint public ise true döner
    public Predicate<ServerHttpRequest> isOpenApi =
            request -> openApiEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().startsWith(uri)); // startsWith kullanarak daha hassas eşleşme
}
