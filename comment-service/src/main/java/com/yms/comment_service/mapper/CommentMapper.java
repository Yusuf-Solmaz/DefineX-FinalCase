package com.yms.comment_service.mapper;

import com.yms.comment_service.dto.request.CommentCreateRequest;
import com.yms.comment_service.dto.response.CommentResponse;
import com.yms.comment_service.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "deleted", target = "isDeleted")
    CommentResponse toCommentDto(Comment comment);

    Comment toComment(CommentCreateRequest response, String userEmail);
}
