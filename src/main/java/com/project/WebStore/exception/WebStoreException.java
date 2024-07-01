package com.project.WebStore.exception;

import lombok.Getter;

@Getter
public class WebStoreException extends RuntimeException {
  private final int statusValue;
  private final String message;

  public WebStoreException(ErrorCode errorCode) {
    this.statusValue = errorCode.getStatusValue();
    this.message = errorCode.getMessage();
  }
}
