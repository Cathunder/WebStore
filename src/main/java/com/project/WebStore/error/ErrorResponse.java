package com.project.WebStore.error;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

  private final int statusCode;
  private final String message;
  private List<ErrorField> errors;

  public static ErrorResponse of(int statusCode, String message) {
    return ErrorResponse.builder().statusCode(statusCode).message(message).build();
  }

  public static ErrorResponse of(int statusCode, String message, List<ErrorField> errors) {
    return ErrorResponse.builder()
        .statusCode(statusCode)
        .message(message)
        .errors(errors)
        .build();
  }
}
