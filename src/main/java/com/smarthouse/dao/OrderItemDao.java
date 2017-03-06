package com.smarthouse.dao;

import com.smarthouse.dao.rowMapper.OrderItemRowMapper;
import com.smarthouse.pojo.OrderItem;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderItemDao extends MajorDao{

    public OrderItem get(int id) {
        return getJdbcTemplate()
                .queryForObject("SELECT * FROM orderitem WHERE ID = ?",
                        new Object[]{id},
                        new OrderItemRowMapper());
    }

    public List<OrderItem> getAll() {
        return getJdbcTemplate()
                .query("SELECT * FROM orderitem",
                        new OrderItemRowMapper());
    }

    public List<OrderItem> getByProductCard(String productCardSku) {
        List<OrderItem> orderItemList = (List<OrderItem>) getJdbcTemplate()
                .query("SELECT  * FROM orderitem WHERE PRODUCTCARD = ?",
                        new Object[]{productCardSku},
                        new OrderItemRowMapper());
        if (orderItemList.size() == 0)
            throw new NoSuchElementException();
        return orderItemList;
    }

    public List<OrderItem> getByOrderMain(int orderMainId) {
        List<OrderItem> orderItemList = (List<OrderItem>) getJdbcTemplate()
                .query("SELECT  * FROM orderitem WHERE ORDERMAIN = ?",
                        new Object[]{orderMainId},
                        new OrderItemRowMapper());
        if (orderItemList.size() == 0)
            throw new NoSuchElementException();
        return orderItemList;
    }

    public OrderItem add(OrderItem orderItem) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(con -> {
            PreparedStatement ps;
            ps = con.prepareStatement("INSERT INTO orderitem (AMOUNT , TOTALPRICE, ORDERMAIN, PRODUCTCARD) VALUES(?,?,?,?)", new String[]{"id"});
            ps.setInt(1, orderItem.getAmount());
            ps.setInt(2, orderItem.getTotalprice());
            ps.setInt(3, orderItem.getOrderMainId());
            ps.setString(4, orderItem.getProductCardSku());

            return ps;
        }, keyHolder);
        return get(keyHolder.getKey().intValue());
    }

    public OrderItem update(OrderItem orderItem) {

        getJdbcTemplate().update("UPDATE orderitem SET AMOUNT = ?, TOTALPRICE = ?, ORDERMAIN = ?, PRODUCTCARD = ? WHERE ID = ?",
                orderItem.getAmount(), orderItem.getTotalprice(), orderItem.getOrderMainId(), orderItem.getProductCardSku(), orderItem.getId());

        return get(orderItem.getId());
    }

    public void delete(int id) {
        getJdbcTemplate().update("DELETE FROM orderitem WHERE ID = ?", id);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM orderitem");
    }
}
