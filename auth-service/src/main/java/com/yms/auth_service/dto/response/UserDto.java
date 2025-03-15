package com.yms.auth_service.dto.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record UserDto(
        Integer id,
        String firstname,
        String lastname,
        String email,
        List<String> authorities,
        String fullName
) {
}
