package com.smarthouse.dao;

import com.smarthouse.dao.rowMapper.CustomerRowMapper;
import com.smarthouse.pojo.Customer;
import java.util.List;

public class CustomerDao extends MajorDao{

    public Customer get(String email) {
        return  getJdbcTemplate()
                .queryForObject("SELECT * FROM customer WHERE EMAIL = ?",
                        new Object[]{email},
                        new CustomerRowMapper());
    }

    public List<Customer> getAll() {
        return getJdbcTemplate()
                .query("SELECT * FROM customer",
                        new CustomerRowMapper());
    }

    public Customer add(Customer customer) {
        getJdbcTemplate().update("INSERT INTO customer (EMAIL, NAME, PHONE, SUBSCRIBE) VALUES(?,?,?,?)",
                customer.getEmail(), customer.getName(), customer.getPhone(), customer.isSubscribe());
        return get(customer.getEmail());
    }

    public Customer update(Customer customer) {
        getJdbcTemplate().update("UPDATE customer SET NAME = ?, PHONE = ?, SUBSCRIBE = ? WHERE EMAIL = ?",
                customer.getName(), customer.getPhone(), customer.isSubscribe(), customer.getEmail());
        return get(customer.getEmail());
    }

    public void delete(String email) {
        getJdbcTemplate().update("DELETE FROM customer WHERE EMAIL= ?",  email);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM customer");
    }

    public boolean isEmailExist(String email) {
        List<Customer> customerList = (List<Customer>) getJdbcTemplate()
                .query("SELECT * FROM customer WHERE EMAIL = ?",
                        new Object[]{email},
                        new CustomerRowMapper());
        return customerList.size() > 0;
    }
}
