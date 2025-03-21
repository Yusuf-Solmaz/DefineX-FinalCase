package com.yms.projectservice.exception;

public class InvalidTeamMemberException extends RuntimeException {
    public InvalidTeamMemberException(String message) {
        super(message);
    }
}
