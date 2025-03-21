package com.yms.auth_service.service.abstracts;

import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse getAuthenticatedUser();
    void updateAuthenticatedUser(RegistrationRequest updatedUser);
    void changeUserAuthority(List<String> roleNames, Integer userId);
    UserResponse findById(Integer id);
    List<UserResponse> findByIds(List<Integer> ids);
    PagedResponse<UserResponse> getAllActiveUsers(Pageable pageable);
    void deleteById(Integer id);

}
