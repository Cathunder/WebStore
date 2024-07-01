package com.project.WebStore.exception;

import static com.project.WebStore.exception.ErrorCode.EMAIL_NOT_FOUND;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebStoreException.class)
  protected ResponseEntity<ErrorResponse> handleWebStoreException(WebStoreException e) {
    log.error("WebStoreException is occurred", e);

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .statusValue(e.getStatusValue())
            .message(e.getMessage())
            .build();

    return ResponseEntity.status(e.getStatusValue()).body(errorResponse);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
    log.error("UsernameNotFoundException is occurred", e);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .statusValue(EMAIL_NOT_FOUND.getStatusValue())
        .message(e.getMessage())
        .build();

    return ResponseEntity.status(EMAIL_NOT_FOUND.getStatusValue()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Exception is occurred", e);

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .statusValue(status.value())
            .message(status.getReasonPhrase())
            .build();

    return ResponseEntity.status(status).body(errorResponse);
  }
}
