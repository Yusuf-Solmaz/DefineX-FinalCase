package com.yms.comment_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentRequest;
import com.yms.comment_service.dto.PagedResponse;
import com.yms.comment_service.dto.TaskResponse;
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

    private CommentRequest commentRequest;
    private Comment comment;
    private CommentDto commentDto;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        commentRequest = CommentRequest.builder()
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

        commentDto = CommentDto.builder()
                .taskId(1)
                .userEmail("user@example.com")
                .content("Test comment content")
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
    }

    @Test
    void testAddComment_ShouldAddAndReturnDto() {
        // Arrange
        when(taskClient.findTaskById(anyInt(), anyString())).thenReturn(taskResponse);
        when(commentMapper.toComment(any(CommentRequest.class), anyString())).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentDto(any(Comment.class))).thenReturn(commentDto);

        // Act
        CommentDto result = commentService.addComment(commentRequest, "user@example.com", "dummyToken");

        // Assert
        assertNotNull(result);
        assertEquals(commentDto.content(), result.content());
        verify(taskClient).findTaskById(anyInt(), anyString());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testGetCommentsByTaskId_ShouldReturnPagedResponse() {
        // Arrange
        Page<Comment> commentPage = new PageImpl<>(List.of(comment));
        Pageable pageable = PageRequest.of(0, 10);

        when(commentMapper.toCommentDto(any(Comment.class))).thenReturn(commentDto);
        when(commentRepository.findAllByTaskIdAndIsDeletedFalse(anyInt(), eq(pageable)))
                .thenReturn(commentPage);

        // Act
        PagedResponse<CommentDto> result = commentService.getCommentsByTaskId(1, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(commentDto.content(), result.getContent().getFirst().content());
        verify(commentRepository).findAllByTaskIdAndIsDeletedFalse(anyInt(), eq(pageable));
    }

    @Test
    void testDeleteComment_ShouldDeleteComment() {
        // Arrange
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

        // Act
        commentService.deleteComment("1");

        // Assert
        assertTrue(existingComment.getIsDeleted());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(commentRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentNotFound.class, () -> commentService.deleteComment("nonExistentId"));
    }
}

