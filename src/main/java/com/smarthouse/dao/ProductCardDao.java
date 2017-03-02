package com.smarthouse.dao;

import com.smarthouse.pojo.ProductCard;

import java.util.List;

public interface ProductCardDao{

    ProductCard get(String sku);

    List<ProductCard> getByCategory(int categoryId);

    List<ProductCard> getAll();

    ProductCard add(ProductCard productCard);

    ProductCard update(ProductCard productCard);

    void delete(String sku);

    void deleteAll();
}
