package com.yms.auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record RegistrationRequest(

        @NotEmpty(message = "Name is mandatory")
        @NotNull(message = "Name is mandatory")
        @Size(min = 3, message = "Name should be 3 characters long minimum")
        String firstname,

        @Size(min = 3, message = "Lastname should be 3 characters long minimum")
        @NotEmpty(message = "Lastname is mandatory")
        @NotNull(message = "Lastname is mandatory")
        String lastname,

        @Email(message = "Email is not well formatted")
        @NotEmpty(message = "Email is mandatory")
        @NotNull(message = "Email is mandatory")
        String email,

        @NotEmpty(message = "Password is mandatory")
        @NotNull(message = "Password is mandatory")
        @Size(min = 8, message = "Password should be 8 characters long minimum")
        String password) {
}

