package com.yms.auth_service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record RegistrationRequest(
    String firstname,
    String lastname,
    String email,
    String password){}

