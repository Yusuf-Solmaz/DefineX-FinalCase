package com.yms.task_service.exception;

public class UserServiceUnavailableException extends RuntimeException {
  public UserServiceUnavailableException(String message) {
    super(message);
  }
}
