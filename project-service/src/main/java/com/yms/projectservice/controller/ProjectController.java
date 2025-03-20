package com.yms.projectservice.controller;

import com.yms.projectservice.dto.PagedResponse;
import com.yms.projectservice.dto.ProjectRequest;
import com.yms.projectservice.dto.UserResponse;
import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.service.abstracts.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
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
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody @Valid ProjectRequest projectRequest,
            @RequestHeader("Authorization") String token) {

        ProjectDto savedProject = projectService.save(projectRequest, token);

        return ResponseEntity.created(
                URI.create("/api/v1/projects/" + savedProject.id())
        ).body(savedProject);
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


}
