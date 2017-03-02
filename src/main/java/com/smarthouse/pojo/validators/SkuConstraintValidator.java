package com.smarthouse.pojo.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SkuConstraintValidator implements ConstraintValidator<Sku, String> {

    @Override
    public void initialize(Sku sku) {
    }

    @Override
    public boolean isValid(String skuField, ConstraintValidatorContext cxt) {
        return skuField != null && skuField.matches("[a-zA-Z0-9()-\\.]*");
    }

}
