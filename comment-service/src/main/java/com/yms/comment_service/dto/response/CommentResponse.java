package com.yms.comment_service.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        String id,
        Integer taskId,
        String userEmail,
        String content,
        LocalDateTime createdAt,
        boolean isDeleted
){
}
