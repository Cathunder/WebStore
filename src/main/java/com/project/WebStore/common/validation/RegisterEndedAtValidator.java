package com.project.WebStore.common.validation;

import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto.Request;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterEndedAtValidator implements ConstraintValidator<ValidEndedAt, RegisterPointBoxItemDto.Request> {

  @Override
  public boolean isValid(Request request, ConstraintValidatorContext context) {
    if (request.getEndedAt().isBefore(request.getStartedAt())
        || request.getEndedAt().isEqual(request.getStartedAt())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("종료일은 시작일 이후여야 합니다.")
          .addPropertyNode("endedAt")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
