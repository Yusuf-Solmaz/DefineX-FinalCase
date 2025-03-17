package com.yms.comment_service.service.abstracts;

import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentResponse;
import com.yms.comment_service.dto.PagedResponse;
import org.springframework.data.domain.Pageable;


public interface CommentService {
    CommentDto addComment(CommentResponse response, String userEmail,String token);
    PagedResponse<CommentDto> getCommentsByTaskId(Integer taskId, Pageable pageable);
}
