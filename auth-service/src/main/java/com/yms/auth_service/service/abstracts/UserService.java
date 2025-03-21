package com.yms.auth_service.service.abstracts;

import com.yms.auth_service.dto.request.UserUpdateRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse getAuthenticatedUser();
    void changeUserAuthority(List<String> roleNames, Integer userId);
    UserResponse findById(Integer id);
    List<UserResponse> findByIds(List<Integer> ids);
    PagedResponse<UserResponse> getAllActiveUsers(Pageable pageable);
    void deleteById(Integer id);
    UserResponse updateAuthenticatedUser(UserUpdateRequest updatedUser) throws MessagingException;
    void updateUser(Integer id, UserUpdateRequest updatedUser) throws MessagingException;

}
