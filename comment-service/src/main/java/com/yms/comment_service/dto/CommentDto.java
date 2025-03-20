package com.yms.comment_service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDto (
        Integer taskId,
        String userEmail,
        String content,
        LocalDateTime createdAt
){
}
