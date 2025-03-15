package com.yms.auth_service.controller;

import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.UserDto;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.service.abstracts.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyAuthority('ROLE_PROJECT_MANAGER', 'ROLE_TEAM_LEADER')")
@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        authenticationService.register(request,"ROLE_PROJECT_MANAGER");
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/change-authority/{userId}")
    public ResponseEntity<String> changeUserAuthority(@RequestBody List<String> roleNames, @PathVariable Integer userId) {
        userService.changeUserAuthority(roleNames, userId);
        return ResponseEntity.ok("User roles updated successfully!");
    }


}
