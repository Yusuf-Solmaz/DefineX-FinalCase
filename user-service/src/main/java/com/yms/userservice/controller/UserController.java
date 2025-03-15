package com.yms.userservice.controller;

import com.yms.userservice.dto.UserDto;
import com.yms.userservice.entities.User;
import com.yms.userservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PreAuthorize("hasAuthority('ROLE_TEAM_MEMBERRRR')")
    @GetMapping
    public List<UserDto> findAll(HttpServletRequest request) {
        return userService.findAll();

        /*
        * // HTTP başlıklarından kullanıcının rollerini alıyoruz
        String rolesHeader = request.getHeader("X-User-Roles");
        System.out.println("rolesHeader: " + rolesHeader);

        if (rolesHeader != null) {
            List<String> roles = Arrays.asList(rolesHeader.split(","));
            if (roles.contains("ROLE_TEAM_MEMBER")) {
                return userService.findAll();
            }
        }

        throw new RuntimeException("Unauthorized access");
        * */
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById( @PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> save(@RequestBody User user) {
        UserDto dto = userService.save(user);
        return ResponseEntity.created(URI.create("users"+"/" + user.getId())).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/batch")
    public List<UserDto> findUsersByIds(@RequestParam List<Long> ids) {
        return userService.findByIds(ids);
    }
}
