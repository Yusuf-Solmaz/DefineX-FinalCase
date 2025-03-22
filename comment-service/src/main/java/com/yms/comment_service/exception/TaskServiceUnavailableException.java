package com.yms.comment_service.exception;

public class TaskServiceUnavailableException extends RuntimeException {
  public TaskServiceUnavailableException(String message) {
    super(message);
  }
}
