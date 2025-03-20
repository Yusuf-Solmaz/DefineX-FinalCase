package com.yms.projectservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Project title cannot be empty.")
    @Size(min = 3, max = 100, message = "Project title must be between 3 and 100 characters.")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 10, message = "Project description must be at least 10 characters long.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "project_members", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "member_id")
    private List<Integer> teamMemberIds;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private String departmentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;
}
