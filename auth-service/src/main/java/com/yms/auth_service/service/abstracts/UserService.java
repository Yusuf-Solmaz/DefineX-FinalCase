package com.yms.auth_service.service.abstracts;

import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.UserDto;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    UserDto getAuthenticatedUser();
    void updateAuthenticatedUser(RegistrationRequest updatedUser);
    void deleteAuthenticatedUser(HttpServletRequest request, HttpServletResponse response);
    void changeUserAuthority(List<String> roleNames, Integer userId);
    UserDto findById(Integer id);

    List<UserDto> findByIds(List<Integer> ids);
}
