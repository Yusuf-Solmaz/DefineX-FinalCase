package com.yms.projectservice.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
public record UserResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        List<String> authorities,
        String fullName
) {
}
