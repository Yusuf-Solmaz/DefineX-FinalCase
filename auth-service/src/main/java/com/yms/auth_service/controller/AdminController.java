package com.yms.auth_service.controller;

import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.request.UserUpdateRequest;
import com.yms.auth_service.service.abstracts.AuthenticationService;
import com.yms.auth_service.service.abstracts.UserService;
import com.yms.auth_service.utils.Role;
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
        authenticationService.register(request, Role.ROLE_PROJECT_MANAGER.name());
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/change-authority/{userId}")
    public ResponseEntity<String> changeUserAuthority(@RequestBody List<String> roleNames, @PathVariable Integer userId) {
        userService.changeUserAuthority(roleNames, userId);
        return ResponseEntity.ok("User roles updated successfully!");
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String>  updateUser(@PathVariable Integer userId, @RequestBody @Valid UserUpdateRequest request) throws MessagingException {
        userService.updateUser(userId, request);
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable  Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
