package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.Customer;
import com.smarthouse.pojo.OrderMain;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMainRowMapper implements RowMapper<OrderMain> {

    //o.ORDERID, o.ADDRESS, o.STATUS, o.CUSTOMER, c.NAME, c.PHONE, c.SUBSCRIBE
    @Override
    public OrderMain mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<OrderMain> extractor = rs1 -> {

            Customer customer = new Customer();
            customer.setEmail(rs1.getString(4));
            customer.setName(rs1.getString(5));
            customer.setPhone(rs1.getString(6));
            customer.setSubscribe(rs1.getBoolean(7));

            OrderMain orderMain = new OrderMain();
            orderMain.setOrderId(rs1.getInt(1));
            orderMain.setAddress(rs1.getString(2));
            orderMain.setStatus(rs1.getInt(3));
            orderMain.setCustomer(customer);
            return orderMain;
        };

        return extractor.extractData(rs);
    }
}