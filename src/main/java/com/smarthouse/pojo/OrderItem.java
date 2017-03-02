package com.smarthouse.pojo;

public class OrderItem {

    private int id;

    private int amount;

    private int totalprice;

    String productCardSku;

    int orderMainId;

    public OrderItem() {
    }

    public OrderItem(int amount, int totalprice, String productCardSku, int orderMainId) {
        this.amount = amount;
        this.totalprice = totalprice;
        this.productCardSku = productCardSku;
        this.orderMainId = orderMainId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getProductCardSku() {
        return productCardSku;
    }

    public void setProductCardSku(String productCardSku) {
        this.productCardSku = productCardSku;
    }

    public int getOrderMainId() {
        return orderMainId;
    }

    public void setOrderMainId(int orderMainId) {
        this.orderMainId = orderMainId;
    }
}
