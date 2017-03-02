package com.smarthouse.pojo.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SkuConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Sku {

    String message() default "{Sku}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

