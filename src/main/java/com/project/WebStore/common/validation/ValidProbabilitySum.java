package com.project.WebStore.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProbabilitySumValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProbabilitySum {
  String message() default "Invalid Probability Sum";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
