package com.yms.comment_service.mapper;

import com.yms.comment_service.dto.request.CommentCreateRequest;
import com.yms.comment_service.dto.response.CommentResponse;
import com.yms.comment_service.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponse toCommentDto(Comment comment);
    Comment toComment(CommentCreateRequest response, String userEmail);
}
