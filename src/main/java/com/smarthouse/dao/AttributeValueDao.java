package com.smarthouse.dao;

import com.smarthouse.pojo.AttributeValue;

import java.util.List;

public interface AttributeValueDao {

    AttributeValue get(int id);

    List<AttributeValue> getAll();

    List<AttributeValue> getByProductCard(String productCardSku);

    List<AttributeValue> getByAttributeName(String attributeName);

    AttributeValue add(AttributeValue attributeValue);

    AttributeValue update(AttributeValue attributeValue);

    void delete(int id);

    void deleteAll();
}
