package com.project.WebStore.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // 공통
  INTERNAL_SERVER_ERROR("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR.value()),
  DUPLICATED_EMAIL("이미 등록된 이메일입니다.", HttpStatus.CONFLICT.value()),
  EMAIL_NOT_FOUND("존재하지 않는 이메일입니다.", HttpStatus.BAD_REQUEST.value()),
  PASSWORD_INCORRECT("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED.value()),
  ;

  private final String message;
  private final int statusValue;
}
