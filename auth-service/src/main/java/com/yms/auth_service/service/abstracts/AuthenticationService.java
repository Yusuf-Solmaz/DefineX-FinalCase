package com.yms.auth_service.service.abstracts;

import com.yms.auth_service.dto.request.AuthenticationRequest;
import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationRequest request, String role) throws MessagingException;
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void activateAccount(String token) throws MessagingException;
}
