package com.yms.task_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
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

    @Column(name = "project_id",nullable = false)
    private Integer projectId;

    @ElementCollection
    @CollectionTable(name = "task_members", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "assignee_id")
    private List<Integer> assigneeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    private String reason;
}
