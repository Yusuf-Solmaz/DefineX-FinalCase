package com.yms.auth_service.controller;

import com.yms.auth_service.dto.request.UserUpdateRequest;
import com.yms.auth_service.dto.response.PagedResponse;
import com.yms.auth_service.dto.response.UserResponse;
import com.yms.auth_service.service.abstracts.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserResponse> getAuthenticatedUser() {
        UserResponse userResponse = userService.getAuthenticatedUser();
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping()
    public ResponseEntity<UserResponse> updateAuthenticatedUser(@RequestBody @Valid UserUpdateRequest request) throws MessagingException {
        UserResponse response = userService.updateAuthenticatedUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Integer id) {
        UserResponse userResponse = userService.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/batch")
    public List<UserResponse> findUsersByIds(@RequestParam List<Integer> ids) {
        return userService.findByIds(ids);
    }

    @GetMapping("/actives")
    public ResponseEntity<PagedResponse<UserResponse>> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllActiveUsers(pageable));
    }
}
