package com.yms.comment_service.mapper;

import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentResponse;
import com.yms.comment_service.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);
    Comment toComment(CommentResponse response, String userEmail);
}
