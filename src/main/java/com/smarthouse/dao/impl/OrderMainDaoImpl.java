package com.smarthouse.dao.impl;

import com.smarthouse.dao.OrderMainDao;
import com.smarthouse.dao.rowMapper.OrderMainRowMapper;
import com.smarthouse.pojo.OrderMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderMainDaoImpl implements OrderMainDao{

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public OrderMain get(int orderId) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<OrderMain> orderMainList = (List<OrderMain>) select
                .query("SELECT  ORDERID, ADDRESS, STATUS, CUSTOMER FROM ordermain WHERE ORDERID = ?",
                        new Object[]{orderId},
                        new OrderMainRowMapper());
        if (orderMainList.size() == 0)
            throw new NoSuchElementException();
        return orderMainList.get(0);
    }

    @Override
    public List<OrderMain> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM ordermain",
                        new OrderMainRowMapper());
    }

    @Override
    public List<OrderMain> getByCustomer(String customerEmail) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<OrderMain> orderMainList = (List<OrderMain>) select
                .query("SELECT  * FROM ordermain WHERE CUSTOMER = ?",
                        new Object[]{customerEmail},
                        new OrderMainRowMapper());
        if (orderMainList.size() == 0)
            throw new NoSuchElementException();
        return orderMainList;
    }

    @Override
    public OrderMain add(OrderMain orderMain) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update(con -> {
            PreparedStatement ps;
                ps = con.prepareStatement("INSERT INTO ordermain (ADDRESS, STATUS, CUSTOMER) VALUES(?,?,?)", new String[]{"orderid"});
                ps.setString(1, orderMain.getAddress());
                ps.setInt(2, orderMain.getStatus());
                ps.setString(3, orderMain.getCustomerEmail());
            return ps;
        }, keyHolder);
        return get(keyHolder.getKey().intValue());
    }

    @Override
    public OrderMain update(OrderMain orderMain) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update("UPDATE ordermain SET ADDRESS = ?, STATUS = ?, CUSTOMER = ? WHERE ORDERID = ?",
                new Object[]{orderMain.getAddress(), orderMain.getStatus(), orderMain.getCustomerEmail(), orderMain.getOrderId()});

        return get(orderMain.getOrderId());
    }

    @Override
    public void delete(int orderId) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM ordermain WHERE ORDERID = ?",
                new Object[]{orderId});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM ordermain");
    }
}
