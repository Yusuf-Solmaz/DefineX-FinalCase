package com.yms.comment_service.dto;

import lombok.Builder;

@Builder
public record CommentRequest(
        Integer taskId,
        String content
) {
}
