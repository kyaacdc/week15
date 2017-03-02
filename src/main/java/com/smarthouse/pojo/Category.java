package com.smarthouse.pojo;

import com.smarthouse.pojo.validators.Name;

public class Category {

    private int id;

    private String description;

    @Name
    private String name;

    private int categoryId;

    public Category() {
    }

    public Category(String description, String name, int categoryId) {
        this.description = description;
        this.name = name;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}