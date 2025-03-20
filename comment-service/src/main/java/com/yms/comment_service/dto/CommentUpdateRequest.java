package com.yms.comment_service.dto;

import lombok.Builder;

@Builder
public record CommentUpdateRequest(
        String content
) {}