package com.yms.comment_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yms.comment_service.dto.request.CommentCreateRequest;
import com.yms.comment_service.dto.request.CommentUpdateRequest;
import com.yms.comment_service.dto.response.CommentResponse;
import com.yms.comment_service.dto.response.PagedResponse;
import com.yms.comment_service.dto.response.TaskResponse;
import com.yms.comment_service.entity.Comment;
import com.yms.comment_service.exception.CommentNotFound;
import com.yms.comment_service.mapper.CommentMapper;
import com.yms.comment_service.repository.CommentRepository;
import com.yms.comment_service.service.CommentServiceImpl;
import com.yms.comment_service.service.abstracts.TaskClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskClientService taskClient;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentCreateRequest commentCreateRequest;
    private Comment comment;
    private Comment existingComment;
    private Comment updatedComment;

    private CommentResponse commentResponse;
    private CommentResponse updatedCommentResponse;
    private TaskResponse taskResponse;
    private CommentUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        updateRequest = new CommentUpdateRequest("Updated content");

        commentCreateRequest = CommentCreateRequest.builder()
                .taskId(1)
                .content("Test comment content")
                .build();

        comment = Comment.builder()
                .id("1")
                .taskId(1)
                .userEmail("user@example.com")
                .content("Test comment content")
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        existingComment = Comment.builder()
                .id("1")
                .taskId(1)
                .userEmail("user@example.com")
                .content("Old comment content")
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        updatedComment = Comment.builder()
                .id("1")
                .taskId(1)
                .userEmail("user@example.com")
                .content("Updated comment content")
                .createdAt(existingComment.getCreatedAt())
                .isDeleted(false)
                .build();

        commentResponse = CommentResponse.builder()
                .taskId(1)
                .userEmail("user@example.com")
                .content("Updated comment content")
                .createdAt(LocalDateTime.now())
                .build();

        taskResponse = TaskResponse.builder()
                .id(1)
                .title("Task Title")
                .description("Task Description")
                .priority("High")
                .status("Open")
                .reason("No reason")
                .build();

        updatedCommentResponse = CommentResponse.builder()
                .taskId(1)
                .userEmail("user@example.com")
                .content(updateRequest.content())
                .createdAt(existingComment.getCreatedAt())
                .build();
    }

    @Test
    void testUpdateComment_ShouldUpdateAndReturnDto() {



        when(commentRepository.findById("1")).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);
        when(commentMapper.toCommentDto(any(Comment.class))).thenReturn(updatedCommentResponse);

        CommentResponse result = commentService.updateComment("1", updateRequest);

        assertNotNull(result);
        assertEquals(updateRequest.content(), result.content());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_ShouldThrowException_WhenNotFound() {
        CommentUpdateRequest updateRequest = new CommentUpdateRequest("Updated content");
        when(commentRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        assertThrows(CommentNotFound.class, () -> commentService.updateComment("nonExistentId", updateRequest));
    }

    @Test
    void testAddComment_ShouldAddAndReturnDto() {

        when(taskClient.findTaskById(anyInt(), anyString())).thenReturn(taskResponse);
        when(commentMapper.toComment(any(CommentCreateRequest.class), anyString())).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentDto(any(Comment.class))).thenReturn(commentResponse);

        CommentResponse result = commentService.addComment(commentCreateRequest, "user@example.com", "dummyToken");

        assertNotNull(result);
        assertEquals(commentResponse.content(), result.content());
        verify(taskClient).findTaskById(anyInt(), anyString());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testGetCommentsByTaskId_ShouldReturnPagedResponse() {
        Page<Comment> commentPage = new PageImpl<>(List.of(comment));
        Pageable pageable = PageRequest.of(0, 10);

        when(commentMapper.toCommentDto(any(Comment.class))).thenReturn(commentResponse);
        when(commentRepository.findAllByTaskIdAndIsDeletedFalse(anyInt(), eq(pageable)))
                .thenReturn(commentPage);

        PagedResponse<CommentResponse> result = commentService.getCommentsByTaskId(1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(commentResponse.content(), result.getContent().getFirst().content());
        verify(commentRepository).findAllByTaskIdAndIsDeletedFalse(anyInt(), eq(pageable));
    }

    @Test
    void testDeleteComment_ShouldDeleteComment() {
        Comment existingComment = Comment.builder()
                .id("1")
                .taskId(1)
                .userEmail("user@example.com")
                .content("Test comment content")
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        when(commentRepository.findById(anyString())).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        commentService.deleteComment("1");
        assertTrue(existingComment.getIsDeleted());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_ShouldThrowException_WhenNotFound() {
        when(commentRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CommentNotFound.class, () -> commentService.deleteComment("nonExistentId"));
    }
}

