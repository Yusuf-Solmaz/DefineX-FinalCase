package com.yms.comment_service.service;

import com.yms.comment_service.client.TaskClient;
import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentResponse;
import com.yms.comment_service.dto.TaskResponse;
import com.yms.comment_service.entity.Comment;
import com.yms.comment_service.exception.TaskNotFoundException;
import com.yms.comment_service.mapper.CommentMapper;
import com.yms.comment_service.repository.CommentRepository;
import com.yms.comment_service.service.abstracts.CommentService;
import com.yms.comment_service.service.abstracts.TaskClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<CommentDto> getCommentsByTaskId(Integer taskId) {

        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}

