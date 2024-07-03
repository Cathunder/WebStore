package com.project.WebStore.common.validation;

import com.project.WebStore.item.dto.UpdatePointBoxItemDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateEndedAtValidator implements ConstraintValidator<ValidEndedAt, UpdatePointBoxItemDto.Request> {

  @Override
  public boolean isValid(UpdatePointBoxItemDto.Request request, ConstraintValidatorContext context) {
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
