package com.project.WebStore.common.validation;

import com.project.WebStore.common.type.ItemType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ItemTypeValidator implements ConstraintValidator<ValidItemType, ItemType> {

  @Override
  public boolean isValid(ItemType value, ConstraintValidatorContext context) {
    return value != null && Arrays.asList(ItemType.values()).contains(value);
  }
}
