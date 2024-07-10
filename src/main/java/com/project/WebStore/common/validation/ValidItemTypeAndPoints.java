package com.project.WebStore.common.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ItemTypeAndPointsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidItemTypeAndPoints {
  String message() default "Type and Point is not same";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
