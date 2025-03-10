package com.yms.userservice.service.abstracts;

import com.yms.userservice.dto.UserDto;
import com.yms.userservice.entities.User;

import java.util.List;

public interface UserService {
    UserDto save(User user);
    UserDto findById(Long id);
    void deleteById(Long id);
    List<UserDto> findAll();
}
