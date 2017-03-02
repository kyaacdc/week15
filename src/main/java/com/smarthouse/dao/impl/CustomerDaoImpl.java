package com.smarthouse.dao.impl;

import com.smarthouse.dao.CustomerDao;
import com.smarthouse.dao.rowMapper.CustomerRowMapper;
import com.smarthouse.pojo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomerDaoImpl implements CustomerDao {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Customer get(String email) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<Customer> customerList = (List<Customer>) select
                .query("SELECT * FROM customer WHERE EMAIL = ?",
                        new Object[]{email},
                        new CustomerRowMapper());
        if (customerList.size() == 0)
            throw new NoSuchElementException();
        return customerList.get(0);
    }

    @Override
    public List<Customer> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM customer",
                        new CustomerRowMapper());
    }

    @Override
    public Customer add(Customer customer) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);
        insert.update("INSERT INTO customer (EMAIL, NAME, PHONE, SUBSCRIBE) VALUES(?,?,?,?)",
                new Object[]{customer.getEmail(), customer.getName(), customer.getPhone(), customer.isSubscribe()});
        return get(customer.getEmail());
    }

    @Override
    public Customer update(Customer customer) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);
        insert.update("UPDATE customer SET NAME = ?, PHONE = ?, SUBSCRIBE = ? WHERE EMAIL = ?",
                new Object[]{customer.getName(), customer.getPhone(), customer.isSubscribe(), customer.getEmail()});
        return get(customer.getEmail());
    }

    @Override
    public void delete(String email) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM customer WHERE EMAIL= ?",
                new Object[]{email});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM customer");
    }

    @Override
    public boolean isEmailExist(String email) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<Customer> customerList = (List<Customer>) select
                .query("SELECT * FROM customer WHERE EMAIL = ?",
                        new Object[]{email},
                        new CustomerRowMapper());
        return customerList.size() > 0;
    }
}
