package com.yms.apigateway.filter;

import com.yms.apigateway.exception.UnouthorizedEntry;
import com.yms.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;


import com.yms.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            if (validator.isOpenApi.test(request)) {
                return chain.filter(exchange);
            }

            if (validator.isSecured.test(request)) {
                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    try {
                        jwtUtil.validateToken(token);
                        String username = jwtUtil.extractUsername(token);
                        List<String> userRoles = jwtUtil.extractRoles(token);

                        // Endpoint'e erişebilen yetkili rolleri al
                        List<String> allowedRoles = validator.authorizationRequiredEndpoints.entrySet()
                                .stream()
                                .filter(entry -> path.startsWith(entry.getKey()))
                                .map(Map.Entry::getValue)
                                .findFirst()
                                .orElse(Collections.emptyList());


                        boolean hasPermission = userRoles.stream().anyMatch(allowedRoles::contains);
                        if (!hasPermission) {
                            return Mono.error(new RuntimeException("Forbidden: Unauthorized Entry"));
                        }


                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", username)
                                .header("X-User-Roles", String.join(",", userRoles))
                                .build();

                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Invalid Token"));
                    }
                } else {
                    throw new UnouthorizedEntry("Unauthorized Entry Exception");
                }
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
