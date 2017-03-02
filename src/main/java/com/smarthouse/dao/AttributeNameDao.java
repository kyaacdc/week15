package com.smarthouse.dao;

import com.smarthouse.pojo.AttributeName;

import java.util.List;

public interface AttributeNameDao {

    AttributeName get(String name);

    List<AttributeName> getAll();

    AttributeName add(AttributeName attributeName);

    void delete(String name);

    void deleteAll();
}
