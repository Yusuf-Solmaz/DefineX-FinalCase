package com.yms.comment_service.service;

import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentRequest;
import com.yms.comment_service.dto.PagedResponse;
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
    public CommentDto addComment(CommentRequest response, String userEmail, String token) {

        taskClient.findTaskById(response.taskId(),token);

        Comment comment = commentMapper.toComment(response,userEmail);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public PagedResponse<CommentDto> getCommentsByTaskId(Integer taskId, Pageable pageable) {
        Page<CommentDto> commentPage = commentRepository.findAllByTaskIdAndIsDeletedFalse(taskId, pageable)
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
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFound("Comment not found with id: " + commentId));

        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }

}

