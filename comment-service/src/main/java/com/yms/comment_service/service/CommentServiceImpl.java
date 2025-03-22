package com.yms.comment_service.service;

import com.yms.comment_service.dto.request.CommentCreateRequest;
import com.yms.comment_service.dto.response.CommentResponse;
import com.yms.comment_service.dto.request.CommentUpdateRequest;
import com.yms.comment_service.dto.response.PagedResponse;
import com.yms.comment_service.entity.Comment;
import com.yms.comment_service.exception.CommentNotFound;
import com.yms.comment_service.exception.DeletedCommentUpdateException;
import com.yms.comment_service.exception.exception_response.ErrorMessages;
import com.yms.comment_service.mapper.CommentMapper;
import com.yms.comment_service.repository.CommentRepository;
import com.yms.comment_service.service.abstracts.CommentService;
import com.yms.comment_service.service.abstracts.TaskClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskClientService taskClient;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse addComment(CommentCreateRequest response, String userEmail, String token) {

        taskClient.findTaskById(response.taskId(), token);

        Comment comment = commentMapper.toComment(response, userEmail);

        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(savedComment);
    }

    @Override
    public PagedResponse<CommentResponse> getCommentsByTaskId(Integer taskId, Pageable pageable) {
        Page<CommentResponse> commentPage = commentRepository.findAllByTaskIdAndIsDeletedFalse(taskId, pageable)
                .map(commentMapper::toCommentResponse);

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
                .orElseThrow(() -> new CommentNotFound(String.format(ErrorMessages.COMMENT_NOT_FOUND,commentId)));

        if (comment.isDeleted()) {
            throw new DeletedCommentUpdateException(ErrorMessages.DELETED_COMMENT_UPDATE);
        }

        comment.setContent(updateRequest.content());
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toCommentResponse(updatedComment);
    }

    @Override
    public CommentResponse getCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::toCommentResponse)
                .orElseThrow(() -> new CommentNotFound(String.format(ErrorMessages.COMMENT_NOT_FOUND,commentId)));
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFound(String.format(ErrorMessages.COMMENT_NOT_FOUND,commentId)));

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

}

