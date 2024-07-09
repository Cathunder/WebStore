package com.project.WebStore.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // 공통
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

  // 유저, 관리자
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
  DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
  EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
  PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

  // 아이템
  DUPLICATED_NAME(HttpStatus.CONFLICT, "이미 등록된 아이템명입니다."),
  POINT_BOX_ITEM_TYPE_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 포인트 박스 타입입니다."),
  ITEM_TYPE_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 아이템 타입입니다."),
  ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 아이템입니다."),
  SELLING_ITEM_CANNOT_UPDATE(HttpStatus.CONFLICT, "판매중인 상품은 수정할 수 없습니다."),
  ITEM_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "잘못된 타입입니다."),
  SOLD_OUT(HttpStatus.BAD_REQUEST, "품절된 상품입니다."), // 상태코드변경필요
  INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 구매하려는 양보다 부족합니다."), // 상태코드변경필요
  SALE_PERIOD_ENDED(HttpStatus.BAD_REQUEST, "판매 종료된 상품입니다."), // 상태코드변경필요
  NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."), // 상태코드변경필요

  // 히스토리
  HISTORY_TYPE_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 히스토리 타입입니다."),
  ;

  private final HttpStatus status;
  private final String message;
}
