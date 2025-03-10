package com.yms.userservice.exceptions.root_exception;

public record RootException(
        int status,
        String error,
        Long timeStamp){}
