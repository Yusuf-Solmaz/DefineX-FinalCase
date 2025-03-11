package com.yms.projectservice.controller;

import com.yms.projectservice.dto.MemberResponse;
import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.entity.Project;
import com.yms.projectservice.service.abstracts.ProjectService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<ProjectDto> getAllProjects(){
        return projectService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id){
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProjectDto> createProject(@RequestBody Project project){
        return ResponseEntity.created(
                URI.create("/api/v1/projects"+"/"+project.getId())
        ).body(projectService.save(project));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable Long id){
        projectService.deleteById(id);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<List<MemberResponse>> getProjectMember(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getAllMembers(id));
    }

    @GetMapping("/member-ids/{id}")
    public List<Long> getProjectMemberIds(@PathVariable Long id) {
        return projectService.getAllMembersId(id);
    }

}
