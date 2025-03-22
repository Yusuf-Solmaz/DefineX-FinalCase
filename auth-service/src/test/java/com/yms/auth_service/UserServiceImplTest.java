package com.yms.auth_service;

import com.yms.auth_service.activation.AccountActivation;
import com.yms.auth_service.dto.request.UserUpdateRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserResponse;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.entity.User;
import com.yms.auth_service.exception.EmailInUseException;
import com.yms.auth_service.exception.UserNotAuthenticatedException;
import com.yms.auth_service.exception.UserNotFoundException;
import com.yms.auth_service.repository.RoleRepository;
import com.yms.auth_service.repository.UserRepository;
import com.yms.auth_service.service.UserServiceImpl;
import com.yms.auth_service.mapper.UserMapper;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountActivation activation;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@gmail.com");
        testUser.setFirstname("Yusuf");
        testUser.setLastname("Solmaz");
        testUser.setPassword("password");
        testUser.setEnabled(true);

        userUpdateRequest = new UserUpdateRequest("Ali", "Veli", "ali@gmail.com", "newPassword");
    }


    @Test
    void testChangeUserAuthority() {

        Role teamMemberRole = new Role();
        teamMemberRole.setName("ROLE_TEAM_MEMBER");

        Role teamManagerRole = new Role();
        teamManagerRole.setName("ROLE_TEAM_MANAGER");

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        when(roleRepository.findByName("ROLE_TEAM_MEMBER")).thenReturn(Optional.of(teamMemberRole));
        when(roleRepository.findByName("ROLE_TEAM_MANAGER")).thenReturn(Optional.of(teamManagerRole));

        userService.changeUserAuthority(List.of("ROLE_TEAM_MANAGER"), 1);

        assertTrue(testUser.getRoles().contains(teamManagerRole));
        assertFalse(testUser.getRoles().contains(teamMemberRole));
    }


    @Test
    void testUpdateUser() throws MessagingException {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        userService.updateUser(1, userUpdateRequest);

        verify(userRepository, times(1)).save(testUser);
        assertEquals("Ali", testUser.getFirstname());
        assertEquals("ali@gmail.com", testUser.getEmail());
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1));
    }


    @Test
    void testGetAllActiveUsers() {
        when(userRepository.findAllActives(any())).thenReturn(Page.empty());

        PagedResponse<UserResponse> response = userService.getAllActiveUsers(Pageable.unpaged());

        assertNotNull(response);
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    void testDeleteById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        userService.deleteById(1);

        verify(userRepository, times(1)).save(testUser);
        assertTrue(testUser.isDeleted());
    }

    @Test
    void testGetCurrentAuthenticatedUser_UserNotAuthenticated() {
        assertThrows(UserNotAuthenticatedException.class, () -> userService.getAuthenticatedUser());
    }

}
