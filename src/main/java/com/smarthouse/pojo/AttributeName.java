package com.smarthouse.pojo;

public class AttributeName {

    private String name;

    public AttributeName() {
    }

    public AttributeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        AttributeName attributeName = new AttributeName();
        attributeName.setName("hhioi");
        System.out.println(attributeName.getName());
    }
}
