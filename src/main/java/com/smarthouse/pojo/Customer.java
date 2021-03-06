package com.smarthouse.pojo;

public class Customer {

    private String email;

    private String name;

    private boolean subscribe;

    private String phone;

    public Customer() {
    }

    public Customer(String email, String name, boolean subscribe, String phone) {
        this.email = email;
        this.name = name;
        this.subscribe = subscribe;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
