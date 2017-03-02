package com.smarthouse.dao;

import com.smarthouse.pojo.Category;

import java.util.List;

public interface CategoryDao {

    Category get(int id);

    List<Category> getSubCategories(Category category);

    List<Category> getAll();

    Category add(Category category);

    Category update(Category category);

    void delete(int id);

    void deleteAll();
}
