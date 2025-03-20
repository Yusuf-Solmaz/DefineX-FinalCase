package com.yms.comment_service;

import com.yms.comment_service.controller.CommentController;
import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentRequest;
import com.yms.comment_service.dto.PagedResponse;
import com.yms.comment_service.exception.GlobalExceptionHandler;
import com.yms.comment_service.service.abstracts.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllComments_ShouldReturnPagedResponse() throws Exception {
        // Arrange
        CommentDto commentDto = CommentDto.builder()
                .taskId(1)
                .userEmail("test@example.com")
                .content("This is a test comment")
                .createdAt(null)
                .build();

        PagedResponse<CommentDto> pagedResponse = new PagedResponse<>(
                List.of(commentDto),
                0,
                10,
                1L,
                1,
                true
        );

        when(commentService.getCommentsByTaskId(anyInt(), any(Pageable.class))).thenReturn(pagedResponse);

        // Act and Assert
        mockMvc.perform(get("/api/v1/comments/{taskId}", 1)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].content").value("This is a test comment"))
                .andExpect(jsonPath("$.content[0].userEmail").value("test@example.com"));
    }

    @Test
    void testCreateComment_ShouldReturnCreatedComment() throws Exception {
        // Arrange
        CommentRequest commentRequest = CommentRequest.builder()
                .taskId(1)
                .content("This is a test comment")
                .build();

        CommentDto commentDto = CommentDto.builder()
                .taskId(1)
                .userEmail("test@example.com")
                .content("This is a test comment")
                .createdAt(null)
                .build();

        when(commentService.addComment(any(), anyString(), anyString())).thenReturn(commentDto);

        // Act and Assert
        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .header("Authorization", "Bearer token")
                        .header("X-User-Id", "test@example.com"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/comments/1"))
                .andExpect(jsonPath("$.content").value("This is a test comment"))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Test
    void testDeleteComment_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(commentService).deleteComment(anyString());

        // Act and Assert
        mockMvc.perform(delete("/api/v1/comments/{id}", "123"))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment("123");
    }
}

