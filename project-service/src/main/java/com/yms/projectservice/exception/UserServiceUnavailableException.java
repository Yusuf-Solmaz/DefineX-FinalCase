package com.yms.projectservice.exception;

public class UserServiceUnavailableException extends RuntimeException {
  public UserServiceUnavailableException(String message) {
    super(message);
  }
}
