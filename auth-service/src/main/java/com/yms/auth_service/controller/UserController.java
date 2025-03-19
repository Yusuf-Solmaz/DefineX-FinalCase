package com.yms.auth_service.controller;

import brave.Response;
import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserDto;
import com.yms.auth_service.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserDto> getAuthenticatedUser() {
        UserDto userDto = userService.getAuthenticatedUser();
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable  Integer id) {
        userService.deleteById(id);
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

    @GetMapping("/actives")
    public ResponseEntity<PagedResponse<UserDto>> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllActiveUsers(pageable));
    }
}
