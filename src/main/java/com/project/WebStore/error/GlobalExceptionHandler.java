package com.project.WebStore.error;

import static com.project.WebStore.error.ErrorCode.INTERNAL_SERVER_ERROR;

import com.project.WebStore.error.exception.WebStoreException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebStoreException.class)
  protected ResponseEntity<ErrorResponse> handleWebStoreException(WebStoreException e) {
    log.error("WebStoreException is occurred", e);

    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response =
        ErrorResponse.of(errorCode.getStatus().value(), errorCode.getMessage());

    return ResponseEntity.status(errorCode.getStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException is occurred", e);

    BindingResult bindingResult = e.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();

    List<ErrorField> errorFields = fieldErrors.stream()
        .map(ErrorField::from)
        .toList();

    ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "입력값이 유효하지 않습니다.", errorFields);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Exception is occurred", e);

    HttpStatus status = INTERNAL_SERVER_ERROR.getStatus();
    ErrorResponse response =
        ErrorResponse.of(status.value(), e.getMessage());

    return ResponseEntity.status(status).body(response);
  }
}
