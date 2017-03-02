package com.smarthouse.pojo;

public class OrderMain {

    private int orderId;

    private String address;

    private int status;

    private String customerEmail;

    public OrderMain() {
    }

    public OrderMain(String address, int status, String customerEmail) {
        this.address = address;
        this.status = status;
        this.customerEmail = customerEmail;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
