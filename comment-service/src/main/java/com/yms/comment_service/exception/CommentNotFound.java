package com.yms.comment_service.exception;

public class CommentNotFound extends RuntimeException {
    public CommentNotFound(String message) {
        super(message);
    }
}
