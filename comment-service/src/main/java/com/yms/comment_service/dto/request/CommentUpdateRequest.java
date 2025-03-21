package com.yms.comment_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CommentUpdateRequest(
        @NotBlank(message = "Content cannot be empty.")
        @Size(min = 10, message = "Content must be at least 10 characters long.")
        String content
) {}