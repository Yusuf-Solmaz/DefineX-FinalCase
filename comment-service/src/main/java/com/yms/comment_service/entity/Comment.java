package com.yms.comment_service.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String id;

    @NotNull(message = "Task ID cannot be null.")
    private Integer taskId;
    private String userEmail;

    @NotBlank(message = "Content cannot be empty.")
    @Size(min = 10, message = "Content must be at least 10 characters long.")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
    private Boolean isDeleted = false;
}
