package com.project.WebStore.common.validation;

import com.project.WebStore.item.dto.FixedPointDto;
import com.project.WebStore.item.dto.PointBoxItemRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProbabilitySumValidator implements ConstraintValidator<ValidProbabilitySum, PointBoxItemRequest> {

  @Override
  public boolean isValid(PointBoxItemRequest value, ConstraintValidatorContext context) {
    List<FixedPointDto> fixedPointDtos = value.getFixedPointDtos();

    if (fixedPointDtos != null) {
      double sum = fixedPointDtos.stream()
          .mapToDouble(FixedPointDto::getProbability)
          .sum();

      if (sum != 1.0) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("각 포인트의 확률 총합은 1이 되어야 합니다.")
            .addPropertyNode("fixedPoints")
            .addConstraintViolation();
        return false;
      }
    }
    return true;
  }
}
