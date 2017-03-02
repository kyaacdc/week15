package com.smarthouse.dao;

import com.smarthouse.pojo.OrderMain;

import java.util.List;

public interface OrderMainDao {

    OrderMain get(int orderId);

    List<OrderMain> getAll();

    List<OrderMain> getByCustomer(String customerEmail);

    OrderMain add(OrderMain orderMain);

    OrderMain update(OrderMain orderMain);

    void delete(int orderId);

    void deleteAll();
}
