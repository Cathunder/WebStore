package com.project.WebStore.error;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ErrorField {

  private final String field;
  private final String rejectedValue;
  private final String message;

  public ErrorField(FieldError fieldError) {
    this.field = fieldError.getField();
    this.rejectedValue = fieldError.getRejectedValue() == null ? "null" : fieldError.getRejectedValue().toString();
    this.message = fieldError.getDefaultMessage();
  }

  public static ErrorField from(FieldError fieldError) {
    return new ErrorField(fieldError);
  }
}
