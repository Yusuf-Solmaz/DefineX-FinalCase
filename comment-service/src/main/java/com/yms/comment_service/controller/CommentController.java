package com.yms.comment_service.controller;

import com.yms.comment_service.dto.request.CommentCreateRequest;
import com.yms.comment_service.dto.response.CommentResponse;
import com.yms.comment_service.dto.request.CommentUpdateRequest;
import com.yms.comment_service.dto.response.PagedResponse;
import com.yms.comment_service.service.abstracts.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentResponse> createComment(
            @RequestBody @Valid CommentCreateRequest comment,
            @RequestHeader("Authorization") String token,
            HttpServletRequest request) {
        CommentResponse commentResponse = commentService.addComment(comment, getAuthenticatedUserEmail(request),token);

        return ResponseEntity.created(
                URI.create("/api/v1/comments"+"/"+ commentResponse.taskId())
        ).body(commentResponse);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<PagedResponse<CommentResponse>> getAllComments(
            @PathVariable Integer taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable String id,
            @RequestBody @Valid CommentUpdateRequest updateRequest
    ) {
        CommentResponse updatedComment = commentService.updateComment(id, updateRequest);
        return ResponseEntity.ok(updatedComment);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable  String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    private String getAuthenticatedUserEmail(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }
}
