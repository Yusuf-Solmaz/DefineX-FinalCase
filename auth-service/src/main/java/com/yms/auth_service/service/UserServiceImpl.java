package com.yms.auth_service.service;

import com.yms.auth_service.activation.AccountActivation;
import com.yms.auth_service.dto.request.UserUpdateRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserResponse;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.entity.User;
import com.yms.auth_service.exception.EmailInUseException;
import com.yms.auth_service.exception.RoleNotFoundException;
import com.yms.auth_service.exception.UserNotAuthenticatedException;
import com.yms.auth_service.exception.UserNotFoundException;
import com.yms.auth_service.exception.exception_response.ErrorMessages;
import com.yms.auth_service.mapper.UserMapper;
import com.yms.auth_service.repository.RoleRepository;
import com.yms.auth_service.repository.UserRepository;
import com.yms.auth_service.service.abstracts.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AccountActivation activation;

    @Override
    public UserResponse getAuthenticatedUser() {
        User user = getCurrentAuthenticatedUser();
        return userMapper.toDto(user);
    }

    @Override
    public void changeUserAuthority(List<String> roleNames, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,userId)));

        Set<String> uniqueRoleNames = new HashSet<>(roleNames);

        List<Role> roles = uniqueRoleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND, roleName))))
                .collect(Collectors.toList());

        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public UserResponse findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,id)));
    }

    @Override
    public void updateUser(Integer id, UserUpdateRequest updatedUser) throws MessagingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,id)));

        mergeUserDetails(user, updatedUser);
        userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponse updateAuthenticatedUser(UserUpdateRequest updatedUser) throws MessagingException {
        User user = getCurrentAuthenticatedUser();

        mergeUserDetails(user, updatedUser);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserResponse> findByIds(List<Integer> ids) {
        List<User> users = userRepository.findAllById(ids);

        if (users.isEmpty()) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND+ids);
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
                .orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,id)));
        user.setDeleted(true);
        userRepository.save(user);
    }

    private void mergeUserDetails(User user, UserUpdateRequest updatedUser) throws MessagingException {
        if (updatedUser.firstname() != null && !updatedUser.firstname().isBlank()) {
            user.setFirstname(updatedUser.firstname());
        }

        if (updatedUser.lastname() != null && !updatedUser.lastname().isBlank()) {
            user.setLastname(updatedUser.lastname());
        }

        if (updatedUser.email() != null && !updatedUser.email().isBlank()) {

            if (!user.getEmail().equals(updatedUser.email())) {
                if (userRepository.findByEmail(updatedUser.email()).isPresent()) {
                    throw new EmailInUseException(String.format(ErrorMessages.EMAIL_IN_USE,updatedUser.email()));
                }

                user.setEnabled(false);
                user.setEmail(updatedUser.email());

                activation.sendValidationEmail(user);
            }
        }

        if (updatedUser.password() != null && !updatedUser.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.password()));
        }
    }


    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException(ErrorMessages.USER_NOT_AUTHENTICATED);
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND));
    }
}
