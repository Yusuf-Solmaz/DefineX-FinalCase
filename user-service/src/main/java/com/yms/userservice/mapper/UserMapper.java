package com.yms.userservice.mapper;

import com.yms.userservice.dto.UserDto;
import com.yms.userservice.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    List<UserDto> toUserDto(List<User> users);
}
