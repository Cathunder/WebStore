package com.project.WebStore.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {RegisterEndedAtValidator.class, UpdateEndedAtValidator.class})
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEndedAt {

  String message() default "Invalid endedAt";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
