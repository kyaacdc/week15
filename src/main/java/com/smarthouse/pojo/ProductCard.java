package com.smarthouse.pojo;

public class ProductCard {

    private String sku;

    private String name;

    private int price;

    private int amount;

    private int likes;

    private int dislikes;

    private String productDescription;

    private int categoryId;

    public ProductCard() {
    }

    public ProductCard(String sku, String name, int price, int amount, int likes, int dislikes, String productDescription, int categoryId) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.likes = likes;
        this.dislikes = dislikes;
        this.productDescription = productDescription;
        this.categoryId = categoryId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
