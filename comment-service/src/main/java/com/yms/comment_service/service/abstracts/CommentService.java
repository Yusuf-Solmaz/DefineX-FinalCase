package com.yms.comment_service.service.abstracts;

import com.yms.comment_service.dto.CommentCreateRequest;
import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentUpdateRequest;
import com.yms.comment_service.dto.PagedResponse;
import org.springframework.data.domain.Pageable;


public interface CommentService {
    CommentDto addComment(CommentCreateRequest response, String userEmail, String token);
    PagedResponse<CommentDto> getCommentsByTaskId(Integer taskId, Pageable pageable);
    void deleteComment(String commentId);
    CommentDto updateComment(String commentId, CommentUpdateRequest updateRequest);
}
