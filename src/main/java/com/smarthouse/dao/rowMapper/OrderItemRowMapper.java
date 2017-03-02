package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.OrderItem;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<OrderItem> extractor = rs1 -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs1.getInt(1));
            orderItem.setAmount(rs1.getInt(2));
            orderItem.setTotalprice(rs1.getInt(3));
            orderItem.setOrderMainId(rs1.getInt(4));
            orderItem.setProductCardSku(rs1.getString(5));
            return orderItem;
        };

        return extractor.extractData(rs);
    }
}

