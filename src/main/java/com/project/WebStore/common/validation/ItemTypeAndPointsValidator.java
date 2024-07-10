package com.project.WebStore.common.validation;

import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.item.dto.FixedPointDto;
import com.project.WebStore.item.dto.PointBoxItemRequest;
import com.project.WebStore.item.dto.RandomPointDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class ItemTypeAndPointsValidator implements ConstraintValidator<ValidItemTypeAndPoints, PointBoxItemRequest> {

  @Override
  public boolean isValid(PointBoxItemRequest value, ConstraintValidatorContext context) {
    List<FixedPointDto> fixedPointDtos = value.getFixedPointDtos();
    List<RandomPointDto> randomPointDtos = value.getRandomPointDtos();
    ItemType type = value.getType();

    if (fixedPointDtos == null && randomPointDtos == null) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("랜덤형 포인트 또는 고정 포인트 중 하나를 입력해야 합니다.")
          .addPropertyNode("fixedPointDtos")
          .addConstraintViolation();
      context.buildConstraintViolationWithTemplate("랜덤형 포인트 또는 고정 포인트 중 하나를 입력해야 합니다.")
          .addPropertyNode("randomPointDtos")
          .addConstraintViolation();
      return false;
    }

    if (type == RANDOM_POINT_BOX_ITEM) {
      if (fixedPointDtos != null && !fixedPointDtos.isEmpty()) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("랜덤형 포인트 박스에서는 고정 포인트값을 설정할 수 없습니다.")
            .addPropertyNode("fixedPointDtos")
            .addConstraintViolation();
        return false;
      }
    }

    if (type == FIXED_POINT_BOX_ITEM) {
      if (randomPointDtos != null && !randomPointDtos.isEmpty()) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("고정형 포인트 박스에서는 랜덤 포인트값을 설정할 수 없습니다.")
            .addPropertyNode("RandomPointDtos")
            .addConstraintViolation();
        return false;
      }
    }

    return true;
  }
}

