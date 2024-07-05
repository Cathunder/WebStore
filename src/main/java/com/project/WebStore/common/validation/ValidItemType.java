package com.project.WebStore.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ItemTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidItemType {
  String message() default "유효하지 않은 박스형입니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
