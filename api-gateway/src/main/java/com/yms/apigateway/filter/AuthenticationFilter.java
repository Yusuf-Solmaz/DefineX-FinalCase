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

import java.util.List;


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
//@EnableMethodSecurity(securedEnabled = true)
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;  // RouteValidator sınıfını kullanıyoruz

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

            // Eğer istek, açık API endpoint'lerinden birine yöneliyorsa, doğrulama yapılmaz
            if (validator.isOpenApi.test(request)) {
                return chain.filter(exchange);  // Açık API'lere yönlendirme yapılır
            }

            // Eğer istek, authorization gerektiren endpoint'lerden birine yöneliyorsa, token doğrulaması yapılır
            if (validator.isSecured.test(request)) {
                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);  // Token'ı alıyoruz
                    try {
                        jwtUtil.validateToken(token); // Token'ı doğruluyoruz
                        String username = jwtUtil.extractUsername(token);
                        List<String> roles = jwtUtil.extractRoles(token);

                        // Kullanıcı bilgilerini başlıkta ekliyoruz
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", username)
                                .header("X-User-Roles", String.join(",", roles))  // Rolleri virgülle ayırıp başlığa ekliyoruz
                                .build();

                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    } catch (Exception e) {
                        // Token geçersizse hata döndürüyoruz
                        return Mono.error(new RuntimeException("Invalid Token"));
                    }
                } else {

                    throw new UnouthorizedEntry("Unauthorized Entry Exception");
                }
            }

            return chain.filter(exchange);  // Authorization gerekmeyen endpoint'lerde işlem yapılır
        };
    }

    public static class Config {
    }
}
