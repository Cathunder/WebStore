package com.project.WebStore.common.validation;

import static com.project.WebStore.common.type.ItemType.*;

import com.project.WebStore.common.type.ItemType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PointBoxItemTypeValidator implements ConstraintValidator<ValidPointBoxItemType, ItemType> {

  @Override
  public boolean isValid(ItemType value, ConstraintValidatorContext context) {
    return value != null && value != CASH_ITEM && Arrays.asList(values()).contains(value);
  }
}
