package com.smarthouse.dao;

import com.smarthouse.pojo.Customer;

import java.util.List;

public interface CustomerDao {

    Customer get(String email);

    List<Customer> getAll();

    Customer add(Customer customer);

    Customer update(Customer customer);

    void delete(String email);

    void deleteAll();

    boolean isEmailExist(String email);
}
