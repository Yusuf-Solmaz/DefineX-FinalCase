package com.yms.comment_service.dto;

public record CommentResponse(
        Integer taskId,
        String content
) {
}
