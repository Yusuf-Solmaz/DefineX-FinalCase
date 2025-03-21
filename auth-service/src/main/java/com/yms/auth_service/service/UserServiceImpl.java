package com.yms.auth_service.service;

import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserResponse;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.entity.User;
import com.yms.auth_service.exception.RoleNotFoundException;
import com.yms.auth_service.exception.UserNotFoundException;
import com.yms.auth_service.mapper.UserMapper;
import com.yms.auth_service.repository.RoleRepository;
import com.yms.auth_service.repository.TokenRepository;
import com.yms.auth_service.repository.UserRepository;
import com.yms.auth_service.service.abstracts.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse getAuthenticatedUser() {
        User user = getCurrentAuthenticatedUser();
        return userMapper.toDto(user);
    }

    @Override
    public void changeUserAuthority(List<String> roleNames, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toList());

        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public UserResponse findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found!"));
    }


    @Override
    public void updateAuthenticatedUser(RegistrationRequest updatedUser) {
        User user = getCurrentAuthenticatedUser();

        if (!user.getEmail().equals(updatedUser.email()) &&
                userRepository.findByEmail(updatedUser.email()).isPresent()) {
            throw new IllegalStateException("Email is already in use");
        }

        user.setFirstname(updatedUser.firstname());
        user.setLastname(updatedUser.lastname());
        user.setEmail(updatedUser.email());


        if (updatedUser.password() != null && !updatedUser.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.password()));
        }

        userRepository.save(user);
    }



    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<UserResponse> findByIds(List<Integer> ids) {
        List<User> users = userRepository.findAllById(ids);

        if (users.isEmpty()) {
            throw new UserNotFoundException("User with ID " + ids + " not found!");
        }

        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResponse<UserResponse> getAllActiveUsers(Pageable pageable) {

        Page<UserResponse> users = userRepository.findAllActives(pageable)
                .map(userMapper::toDto);
        return new PagedResponse<>(
                users.getContent(),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast()
        );
    }

    @Override
    public void deleteById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found!"));
        user.setDeleted(true);
        userRepository.save(user);
    }
}
