package com.yms.attachment_service.exceptions.root_exception;

public record RootException(
        int status,
        String error,
        Long timeStamp){}
