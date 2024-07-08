package com.project.WebStore.common.validation;

import static com.project.WebStore.common.type.ItemType.*;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.item.dto.FixedPointDto;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.dto.UpdatePointBoxItemDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProbabilitySumValidator implements ConstraintValidator<ValidProbabilitySum, Object> {

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    List<FixedPointDto> fixedPointDtos = null;
    ItemType type = null;

    if (value instanceof RegisterPointBoxItemDto.Request request) {
      fixedPointDtos = request.getFixedPoints();
      type = request.getType();
    } else if (value instanceof UpdatePointBoxItemDto.Request request) {
      fixedPointDtos = request.getFixedPoints();
    } else {
      return false;
    }

    if (type == RANDOM_POINT_BOX_ITEM) {
      if (fixedPointDtos != null && !fixedPointDtos.isEmpty()) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("랜덤형 포인트 박스에서는 고정 포인트값을 설정할 수 없습니다.")
            .addPropertyNode("fixedPoints")
            .addConstraintViolation();
        return false;
      }
      return true;
    }

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
