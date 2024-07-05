package com.project.WebStore.common.validation;

import com.project.WebStore.common.type.PointBoxItemType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ItemTypeValidator implements ConstraintValidator<ValidItemType, PointBoxItemType> {

  @Override
  public boolean isValid(PointBoxItemType value, ConstraintValidatorContext context) {
    return value != null && Arrays.asList(PointBoxItemType.values()).contains(value);
  }
}
