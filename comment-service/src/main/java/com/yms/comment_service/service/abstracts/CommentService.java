package com.yms.comment_service.service.abstracts;

import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentResponse response, String userEmail,String token);
    List<CommentDto> getCommentsByTaskId(Integer taskId);
}
