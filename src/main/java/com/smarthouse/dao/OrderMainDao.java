package com.smarthouse.dao;

import com.smarthouse.dao.rowMapper.OrderMainRowMapper;
import com.smarthouse.pojo.OrderMain;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.util.List;

public class OrderMainDao extends MajorDao{

    public OrderMain get(int orderId) {
        return getJdbcTemplate()
                .queryForObject("SELECT  o.ORDERID, o.ADDRESS, o.STATUS, o.CUSTOMER, c.NAME, c.PHONE, c.SUBSCRIBE " +
                                "FROM ordermain o INNER JOIN customer c ON o.CUSTOMER = c.EMAIL WHERE ORDERID = ?",
                        new Object[]{orderId},
                        new OrderMainRowMapper());
    }

    public List<OrderMain> getAll() {
        return getJdbcTemplate()
                .query("SELECT  o.ORDERID, o.ADDRESS, o.STATUS, o.CUSTOMER, c.NAME, c.PHONE, c.SUBSCRIBE " +
                                "FROM ordermain o INNER JOIN customer c ON o.CUSTOMER = c.EMAIL",
                        new OrderMainRowMapper());
    }

    public List<OrderMain> getByCustomer(String customerEmail) {
        return  getJdbcTemplate()
                .query("SELECT  o.ORDERID, o.ADDRESS, o.STATUS, o.CUSTOMER, c.NAME, c.PHONE, c.SUBSCRIBE " +
                                "FROM ordermain o INNER JOIN customer c ON o.CUSTOMER = c.EMAIL WHERE o.CUSTOMER = ?",
                        new Object[]{customerEmail},
                        new OrderMainRowMapper());
    }

    public OrderMain add(OrderMain orderMain) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(con -> {
            PreparedStatement ps;
                ps = con.prepareStatement("INSERT INTO ordermain (ADDRESS, STATUS, CUSTOMER) VALUES(?,?,?)", new String[]{"orderid"});
                ps.setString(1, orderMain.getAddress());
                ps.setInt(2, orderMain.getStatus());
                ps.setString(3, orderMain.getCustomer().getEmail());
            return ps;
        }, keyHolder);
        return get(keyHolder.getKey().intValue());
    }

    public OrderMain update(OrderMain orderMain) {

        getJdbcTemplate().update("UPDATE ordermain SET ADDRESS = ?, STATUS = ?, CUSTOMER = ? WHERE ORDERID = ?",
                orderMain.getAddress(), orderMain.getStatus(), orderMain.getCustomer().getEmail(), orderMain.getOrderId());

        return get(orderMain.getOrderId());
    }

    public void delete(int orderId) {
        getJdbcTemplate().update("DELETE FROM ordermain WHERE ORDERID = ?", orderId);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM ordermain");
    }
}
