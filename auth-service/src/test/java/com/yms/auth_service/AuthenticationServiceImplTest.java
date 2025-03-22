package com.yms.auth_service;

import com.yms.auth_service.activation.AccountActivation;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.entity.Token;
import com.yms.auth_service.entity.User;
import com.yms.auth_service.dto.request.AuthenticationRequest;
import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.AuthenticationResponse;
import com.yms.auth_service.exception.ActivationTokenException;
import com.yms.auth_service.exception.exception_response.ErrorMessages;
import com.yms.auth_service.repository.RoleRepository;
import com.yms.auth_service.repository.TokenRepository;
import com.yms.auth_service.repository.UserRepository;
import com.yms.auth_service.security.JwtService;
import com.yms.auth_service.service.AuthenticationServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AccountActivation activation;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testRegister() throws MessagingException {

        RegistrationRequest request = new RegistrationRequest("Yusuf", "Solmaz", "Yusuf@gmail.com", "password123");
        Role userRole;
        userRole = new Role();
        userRole.setId(1);
        userRole.setName("ROLE_TEAM_MANAGER");

        when(roleRepository.findByName("ROLE_TEAM_MANAGER")).thenReturn(Optional.of(userRole));

        // When
        authenticationService.register(request, "ROLE_TEAM_MANAGER");

        // Then
        verify(userRepository, times(1)).save(any(User.class));
        verify(activation, times(1)).sendValidationEmail(any(User.class));
    }

    @Test
    void testAuthenticate() {
        // Given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("Yusuf@gmail.com")
                .password("password123")
                .build();

        User testUser = new User();
        testUser.setId(1);
        testUser.setFirstname("Yusuf Solmaz");

        Authentication authentication = new UsernamePasswordAuthenticationToken(testUser, null, List.of());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(any(), any(User.class))).thenReturn("dummy-jwt-token");

        // When
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Then
        assertNotNull(response);
        assertEquals("dummy-jwt-token", response.getToken());
    }

    @Test
    void testActivateAccount() throws MessagingException {
        // Given
        String token = "valid-token";

        // Create the Token object using the builder pattern
        Token savedToken = Token.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(User.builder().id(1).email("test@gmail.com").firstname("Yusuf").lastname("Solmaz").build())
                .build();


        new User();
        User user = User.builder().id(1).email("test@gmail.com").firstname("Yusuf").lastname("Solmaz").enabled(false).build();

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(savedToken));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When
        authenticationService.activateAccount(token);

        // Then
        verify(userRepository, times(1)).save(user);
        assertTrue(user.isEnabled());
        verify(tokenRepository, times(1)).save(savedToken);
        assertNotNull(savedToken.getValidatedAt());
    }

    @Test
    void testActivateAccount_TokenExpired() throws MessagingException {

        String token = "expired-token";


        User user = User.builder()
                .id(1)
                .email("test@gmail.com")
                .firstname("Yusuf")
                .lastname("Solmaz")
                .build();

        Token savedToken = Token.builder()
                .token(token)
                .createdAt(LocalDateTime.now().minusMinutes(1))
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .user(user)
                .build();

        // Mock the token repository
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(savedToken));

        // When
        ActivationTokenException exception = assertThrows(ActivationTokenException.class, () -> authenticationService.activateAccount(token));

        // Then
        assertEquals(ErrorMessages.TOKEN_EXPIRED, exception.getMessage());
        verify(activation, times(1)).sendValidationEmail(user);
    }
}
