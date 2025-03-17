package com.yms.comment_service.service;

import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentResponse;
import com.yms.comment_service.dto.PagedResponse;
import com.yms.comment_service.entity.Comment;
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
    public CommentDto addComment(CommentResponse response, String userEmail,String token) {

        taskClient.findTaskById(response.taskId(),token);

        Comment comment = commentMapper.toComment(response,userEmail);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public PagedResponse<CommentDto> getCommentsByTaskId(Integer taskId, Pageable pageable) {
        Page<CommentDto> commentPage = commentRepository.findAllByTaskId(taskId, pageable)
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
}

