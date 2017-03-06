package com.smarthouse.pojo;

public class Visualization {

    private int id;

    private int type;

    private String url;

    private String productCardSku;

    public Visualization() {
    }

    public Visualization(int type, String url, String productCardSku) {
        this.type = type;
        this.url = url;
        this.productCardSku = productCardSku;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductCardSku() {
        return productCardSku;
    }

    public void setProductCardSku(String productCardSku) {
        this.productCardSku = productCardSku;
    }
}
