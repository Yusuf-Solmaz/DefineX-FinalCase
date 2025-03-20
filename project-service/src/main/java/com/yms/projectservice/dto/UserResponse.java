package com.yms.projectservice.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record UserResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        List<String> authorities,
        String fullName
) {
}
