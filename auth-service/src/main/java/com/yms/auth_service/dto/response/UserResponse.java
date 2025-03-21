package com.yms.auth_service.dto.response;

import java.util.List;

public record UserResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        List<String> authorities,
        String fullName,
        boolean isDeleted
) {
}
