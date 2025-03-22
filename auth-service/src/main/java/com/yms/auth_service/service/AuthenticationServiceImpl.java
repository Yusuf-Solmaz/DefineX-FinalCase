package com.yms.auth_service.service;

import com.yms.auth_service.activation.AccountActivation;
import com.yms.auth_service.dto.request.AuthenticationRequest;
import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.AuthenticationResponse;
import com.yms.auth_service.entity.Token;
import com.yms.auth_service.entity.User;
import com.yms.auth_service.exception.ActivationTokenException;
import com.yms.auth_service.exception.RoleNotFoundException;
import com.yms.auth_service.exception.exception_response.ErrorMessages;
import com.yms.auth_service.repository.RoleRepository;
import com.yms.auth_service.repository.TokenRepository;
import com.yms.auth_service.repository.UserRepository;
import com.yms.auth_service.security.JwtService;
import com.yms.auth_service.service.abstracts.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final AccountActivation activation;


    @Override
    public void register(RegistrationRequest request, String role) throws MessagingException {
        var userRole = roleRepository.findByName(role)
                .orElseThrow(() -> new RoleNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND,role)));

        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        activation.sendValidationEmail(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("id", user.getId());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    @Override
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)

                .orElseThrow(() -> new ActivationTokenException(ErrorMessages.INVALID_TOKEN));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            activation.sendValidationEmail(savedToken.getUser());
            throw new ActivationTokenException(ErrorMessages.TOKEN_EXPIRED);
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,savedToken.getUser().getId())));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

}

