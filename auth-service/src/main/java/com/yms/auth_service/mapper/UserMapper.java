package com.yms.auth_service.mapper;

import com.yms.auth_service.dto.response.UserResponse;
import com.yms.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "authorities", expression = "java(user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toList()))")
    @Mapping(source = "deleted", target = "isDeleted")
    UserResponse toDto(User user);


}
