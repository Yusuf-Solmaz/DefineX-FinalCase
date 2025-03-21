package com.yms.comment_service.service;

import com.yms.comment_service.dto.request.CommentCreateRequest;
import com.yms.comment_service.dto.response.CommentResponse;
import com.yms.comment_service.dto.request.CommentUpdateRequest;
import com.yms.comment_service.dto.response.PagedResponse;
import com.yms.comment_service.entity.Comment;
import com.yms.comment_service.exception.CommentNotFound;
import com.yms.comment_service.mapper.CommentMapper;
import com.yms.comment_service.repository.CommentRepository;
import com.yms.comment_service.service.abstracts.CommentService;
import com.yms.comment_service.service.abstracts.TaskClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskClientService taskClient;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse addComment(CommentCreateRequest response, String userEmail, String token) {

        taskClient.findTaskById(response.taskId(),token);

        Comment comment = commentMapper.toComment(response,userEmail);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public PagedResponse<CommentResponse> getCommentsByTaskId(Integer taskId, Pageable pageable) {
        Page<CommentResponse> commentPage = commentRepository.findAllByTaskIdAndIsDeletedFalse(taskId, pageable)
                .map(commentMapper::toCommentDto);

        return new PagedResponse<>(
                commentPage.getContent(),
                commentPage.getNumber(),
                commentPage.getSize(),
                commentPage.getTotalElements(),
                commentPage.getTotalPages(),
                commentPage.isLast()
        );
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentUpdateRequest updateRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFound("Comment not found with id: " + commentId));

        if (comment.getIsDeleted()) {
            throw new CommentNotFound("Cannot update a deleted comment.");
        }
        if (comment.getContent() != null  && !comment.getContent().isEmpty()) {
            throw new RuntimeException("Content cannot be empty");
        }

        comment.setContent(updateRequest.content());
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFound("Comment not found with id: " + commentId));

        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }

}

