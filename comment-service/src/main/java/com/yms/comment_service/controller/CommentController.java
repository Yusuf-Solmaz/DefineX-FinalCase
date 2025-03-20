package com.yms.comment_service.controller;

import com.yms.comment_service.dto.CommentDto;
import com.yms.comment_service.dto.CommentRequest;
import com.yms.comment_service.dto.PagedResponse;
import com.yms.comment_service.service.abstracts.CommentService;
import jakarta.servlet.ServletRequest;
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

    @GetMapping("{taskId}")
    public ResponseEntity<PagedResponse<CommentDto>> getAllComments(
            @PathVariable Integer taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId, pageable));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid CommentRequest comment, @RequestHeader("Authorization") String token, HttpServletRequest request) {
        CommentDto commentDto = commentService.addComment(comment, getAuthenticatedUserEmail(request),token);

        return ResponseEntity.created(
                URI.create("/api/v1/comments"+"/"+commentDto.taskId())
        ).body(commentDto);
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
