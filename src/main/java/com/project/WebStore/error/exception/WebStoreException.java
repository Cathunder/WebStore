package com.project.WebStore.error.exception;

import com.project.WebStore.error.ErrorCode;
import lombok.Getter;

@Getter
public class WebStoreException extends RuntimeException {

  private final ErrorCode errorCode;

  public WebStoreException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
