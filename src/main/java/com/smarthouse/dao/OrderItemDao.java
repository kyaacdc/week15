package com.smarthouse.dao;

import com.smarthouse.pojo.OrderItem;

import java.util.List;

public interface OrderItemDao {

    OrderItem get(int id);

    List<OrderItem> getAll();

    List<OrderItem> getByProductCard(String productCardSku);

    List<OrderItem> getByOrderMain(int orderMainId);

    OrderItem add(OrderItem orderItem);

    OrderItem update(OrderItem orderItem);

    void delete(int id);

    void deleteAll();

}
