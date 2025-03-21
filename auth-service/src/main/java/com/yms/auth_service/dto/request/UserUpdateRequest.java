package com.yms.auth_service.dto.request;

public record UserUpdateRequest(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
