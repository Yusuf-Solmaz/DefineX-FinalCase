package com.yms.apigateway.exception.root_exception;

public record RootException(
        int status,
        String error,
        Long timeStamp){}
