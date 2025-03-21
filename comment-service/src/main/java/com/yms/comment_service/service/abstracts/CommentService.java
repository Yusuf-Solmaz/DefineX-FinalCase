package com.yms.comment_service.service.abstracts;

import com.yms.comment_service.dto.request.CommentCreateRequest;
import com.yms.comment_service.dto.response.CommentResponse;
import com.yms.comment_service.dto.request.CommentUpdateRequest;
import com.yms.comment_service.dto.response.PagedResponse;
import org.springframework.data.domain.Pageable;


public interface CommentService {
    CommentResponse addComment(CommentCreateRequest response, String userEmail, String token);
    PagedResponse<CommentResponse> getCommentsByTaskId(Integer taskId, Pageable pageable);
    void deleteComment(String commentId);
    CommentResponse updateComment(String commentId, CommentUpdateRequest updateRequest);
}
