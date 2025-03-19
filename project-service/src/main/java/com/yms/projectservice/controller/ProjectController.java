package com.yms.projectservice.controller;

import com.yms.projectservice.dto.PagedResponse;
import com.yms.projectservice.dto.UserResponse;
import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.service.abstracts.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;


    @GetMapping("/all")
    public ResponseEntity<PagedResponse<ProjectDto>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(projectService.findAll(pageable));
    }

    @GetMapping("/actives")
    public ResponseEntity<PagedResponse<ProjectDto>> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(projectService.getAllActiveProjects(pageable));
    }

    @GetMapping("/department/{name}")
    public List<ProjectDto> getProjectsByDepartment(@PathVariable String name) {
        return projectService.findByDepartmentName(name);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Integer id){
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProjectDto> createProject(@RequestBody Project project, @RequestHeader("Authorization") String token){
        return ResponseEntity.created(
                URI.create("/api/v1/projects"+"/"+project.getId())
        ).body(projectService.save(project,token));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable Integer id){
        projectService.deleteById(id);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<List<UserResponse>> getProjectMember(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(projectService.getAllMembers(id,token));
    }

    @GetMapping("/member-ids/{id}")
    public List<Integer> getProjectMemberIds(@PathVariable Integer id) {
        return projectService.getAllMembersId(id);
    }

//    private boolean hasRole(HttpServletRequest request, String requiredRole) {
//        String rolesHeader = request.getHeader("X-User-Roles");
//        System.out.println("rolesHeader: " + rolesHeader);
//        if (rolesHeader != null) {
//            List<String> roles = Arrays.asList(rolesHeader.split(","));
//            return roles.contains(requiredRole);
//        }
//        return false;
//    }
//
//    @GetMapping("/test")
//    public List<ProjectDto> findAll(HttpServletRequest request) {
////        if (hasRole(request, "ROLE_TEAM_MEMBER")) {
////            return projectService.findAll();
////        }
////        throw new RuntimeException("Unauthorized access");
//        return projectService.findAll();
//    }
}
