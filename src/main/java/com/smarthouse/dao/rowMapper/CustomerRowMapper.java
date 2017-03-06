package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.Customer;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<Customer> extractor = rs1 -> {
            Customer customer = new Customer();
            customer.setEmail(rs1.getString(1));
            customer.setName(rs1.getString(2));
            customer.setPhone(rs1.getString(3));
            customer.setSubscribe(rs1.getBoolean(4));
            return customer;
        };

        return extractor.extractData(rs);
    }
}
