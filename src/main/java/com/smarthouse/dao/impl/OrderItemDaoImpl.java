package com.smarthouse.dao.impl;

import com.smarthouse.dao.OrderItemDao;
import com.smarthouse.dao.rowMapper.OrderItemRowMapper;
import com.smarthouse.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderItemDaoImpl implements OrderItemDao {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public OrderItem get(int id) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<OrderItem> attributeValueList = (List<OrderItem>) select
                .query("SELECT * FROM orderitem WHERE ID = ?",
                        new Object[]{id},
                        new OrderItemRowMapper());
        if (attributeValueList.size() == 0)
            throw new NoSuchElementException();
        return attributeValueList.get(0);
    }

    @Override
    public List<OrderItem> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM orderitem",
                        new OrderItemRowMapper());
    }

    @Override
    public List<OrderItem> getByProductCard(String productCardSku) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<OrderItem> orderItemList = (List<OrderItem>) select
                .query("SELECT  * FROM orderitem WHERE PRODUCTCARD = ?",
                        new Object[]{productCardSku},
                        new OrderItemRowMapper());
        if (orderItemList.size() == 0)
            throw new NoSuchElementException();
        return orderItemList;
    }

    @Override
    public List<OrderItem> getByOrderMain(int orderMainId) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<OrderItem> orderItemList = (List<OrderItem>) select
                .query("SELECT  * FROM orderitem WHERE ORDERMAIN = ?",
                        new Object[]{orderMainId},
                        new OrderItemRowMapper());
        if (orderItemList.size() == 0)
            throw new NoSuchElementException();
        return orderItemList;
    }

    @Override
    public OrderItem add(OrderItem orderItem) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update(con -> {
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

    @Override
    public OrderItem update(OrderItem orderItem) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update("UPDATE orderitem SET AMOUNT = ?, TOTALPRICE = ?, ORDERMAIN = ?, PRODUCTCARD = ? WHERE ID = ?",
                new Object[]{orderItem.getAmount(), orderItem.getTotalprice(), orderItem.getOrderMainId(), orderItem.getProductCardSku(), orderItem.getId()});

        return get(orderItem.getId());
    }

    @Override
    public void delete(int id) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM orderitem WHERE ID = ?",
                new Object[]{id});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM orderitem");
    }
}
