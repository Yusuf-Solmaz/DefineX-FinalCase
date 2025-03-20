package com.yms.comment_service.dto;

import lombok.Builder;

@Builder
public record CommentCreateRequest(
        Integer taskId,
        String content
) {
}
