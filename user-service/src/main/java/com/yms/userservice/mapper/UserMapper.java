package com.yms.userservice.mapper;

import com.yms.userservice.dto.UserDto;
import com.yms.userservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    List<UserDto> toUserDto(List<User> users);
}
