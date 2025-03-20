package com.yms.comment_service.dto;

public record CommentRequest(
        Integer taskId,
        String content
) {
}
