package com.yms.comment_service.exception;

public class DeletedCommentUpdateException extends RuntimeException {
    public DeletedCommentUpdateException(String message) {
        super(message);
    }
}
