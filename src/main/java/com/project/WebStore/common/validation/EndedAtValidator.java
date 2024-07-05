package com.project.WebStore.common.validation;

import com.project.WebStore.item.dto.SalePeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndedAtValidator implements ConstraintValidator<ValidEndedAt, SalePeriod> {

  @Override
  public boolean isValid(SalePeriod salePeriod, ConstraintValidatorContext context) {
    if (salePeriod.getEndedAt().isBefore(salePeriod.getStartedAt())
        || salePeriod.getEndedAt().isEqual(salePeriod.getStartedAt())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("종료일은 시작일 이후여야 합니다.")
          .addPropertyNode("endedAt")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
