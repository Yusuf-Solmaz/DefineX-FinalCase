package com.yms.userservice.dto;

public record UserDto(
        Long id,
        String email,
        String fullName
){}
