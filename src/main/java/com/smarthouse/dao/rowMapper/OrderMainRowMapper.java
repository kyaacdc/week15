package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.OrderMain;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMainRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<OrderMain> extractor = rs1 -> {
            OrderMain orderMain = new OrderMain();
            orderMain.setOrderId(rs1.getInt(1));
            orderMain.setAddress(rs1.getString(2));
            orderMain.setStatus(rs1.getInt(3));
            orderMain.setCustomerEmail(rs1.getString(4));
            return orderMain;
        };

        return extractor.extractData(rs);
    }
}
