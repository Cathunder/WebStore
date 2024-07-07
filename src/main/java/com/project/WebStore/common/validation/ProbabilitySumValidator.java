package com.project.WebStore.common.validation;

import com.project.WebStore.item.dto.FixedPointDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class ProbabilitySumValidator implements ConstraintValidator<ValidProbabilitySum, List<FixedPointDto>> {

  @Override
  public boolean isValid(List<FixedPointDto> fixedPointDtos, ConstraintValidatorContext context) {

    double sum = fixedPointDtos.stream()
        .mapToDouble(FixedPointDto::getProbability)
        .sum();

    if (sum != 1.0) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("각 포인트의 확률 총합은 1이 되어야 합니다.")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
