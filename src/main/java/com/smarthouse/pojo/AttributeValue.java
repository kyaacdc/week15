package com.smarthouse.pojo;

import com.smarthouse.pojo.validators.Name;
import com.smarthouse.pojo.validators.Sku;

public class AttributeValue {

    private int id;

    private String value;

    @Name
    private String attributeName;

    @Sku
    private String productCardSku;

    public AttributeValue() {
    }

    public AttributeValue(String value, String attributeName, String productCardSku) {
        this.value = value;
        this.attributeName = attributeName;
        this.productCardSku = productCardSku;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getProductCardSku() {
        return productCardSku;
    }

    public void setProductCardSku(String productCardSku) {
        this.productCardSku = productCardSku;
    }
}
