package com.yms.projectservice.exception;

public class InvalidProjectStatusTransictionException extends RuntimeException {
    public InvalidProjectStatusTransictionException(String message) {
        super(message);
    }
}
