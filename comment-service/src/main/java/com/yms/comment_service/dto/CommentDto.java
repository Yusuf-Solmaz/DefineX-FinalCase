package com.yms.comment_service.dto;

import java.time.LocalDateTime;

public record CommentDto (
        Integer taskId,
        String userEmail,
        String content,
        LocalDateTime createdAt
){
}
