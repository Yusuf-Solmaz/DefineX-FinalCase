package com.yms.auth_service.controller;

import brave.Response;
import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.UserDto;
import com.yms.auth_service.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getAuthenticatedUser() {
        UserDto userDto = userService.getAuthenticatedUser();
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteAuthenticatedUser(request, response);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateAuthenticatedUser(@RequestBody RegistrationRequest request) {
        userService.updateAuthenticatedUser(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Integer id) {
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/batch")
    public List<UserDto> findUsersByIds(@RequestParam List<Integer> ids) {
        return userService.findByIds(ids);
    }
}
