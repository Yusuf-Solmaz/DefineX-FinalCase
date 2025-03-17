package com.yms.comment_service.exception.root_exception;

public record RootException(
        int status,
        String error,
        Long timeStamp){}
