package com.project.WebStore.common.validation;

import com.project.WebStore.item.dto.RandomPointDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxPointValidator implements ConstraintValidator<ValidMaxPoint, RandomPointDto> {

  @Override
  public boolean isValid(RandomPointDto dto, ConstraintValidatorContext context) {

    if(dto.getMinPoint() >= dto.getMaxPoint()){
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("최대포인트는 최소포인트보다 커야합니다.")
          .addPropertyNode("maxPoint")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
