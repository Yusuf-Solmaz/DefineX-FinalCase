package com.yms.projectservice.exception.root_exception;

public record RootException(
        int status,
        String error,
        Long timeStamp){}
