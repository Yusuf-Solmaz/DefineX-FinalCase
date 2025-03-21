package com.yms.comment_service.dto.request;

import lombok.Builder;

@Builder
public record CommentCreateRequest(
        Integer taskId,
        String content
) {
}
