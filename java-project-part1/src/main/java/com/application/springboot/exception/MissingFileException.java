package com.application.springboot.exception;

public class MissingFileException extends Exception {

  public MissingFileException(String message) {
    super(message);
  }

  public MissingFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
