package com.yms.auth_service.service.abstracts;

import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserDto;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDto getAuthenticatedUser();
    void updateAuthenticatedUser(RegistrationRequest updatedUser);
    void changeUserAuthority(List<String> roleNames, Integer userId);
    UserDto findById(Integer id);
    List<UserDto> findByIds(List<Integer> ids);
    PagedResponse<UserDto> getAllActiveUsers(Pageable pageable);
    void deleteById(Integer id);

}
