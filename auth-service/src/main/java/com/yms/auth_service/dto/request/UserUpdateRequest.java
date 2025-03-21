package com.yms.auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 3, message = "Name should be 3 characters long minimum")
        String firstname,

        @Size(min = 3, message = "Lastname should be 3 characters long minimum")
        String lastname,

        @Email(message = "Invalid Email Address")
        String email,

        @Size(min = 8, message = "Password should be 8 characters long minimum")
        String password
) {
}
