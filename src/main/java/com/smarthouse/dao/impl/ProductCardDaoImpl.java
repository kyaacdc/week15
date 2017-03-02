package com.smarthouse.dao.impl;

import com.smarthouse.dao.ProductCardDao;
import com.smarthouse.pojo.ProductCard;
import com.smarthouse.dao.rowMapper.ProductCardRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductCardDaoImpl implements ProductCardDao {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ProductCard get(String sku) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
         List<ProductCard> productCardList = (List<ProductCard>) select
                .query("SELECT  SKU, AMOUNT, DISLIKES, LIKES, NAME, PRICE, PRODUCTDESCRIPTION, CATEGORY FROM productcard WHERE sku = ?",
                        new Object[]{sku},
                        new ProductCardRowMapper());
         if (productCardList.size() == 0)
             throw new NoSuchElementException();
         return productCardList.get(0);
    }

    @Override
    public List<ProductCard> getByCategory(int categoryId) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<ProductCard> productCardList = (List<ProductCard>) select
                .query("SELECT  * FROM productcard WHERE CATEGORY = ?",
                        new Object[]{categoryId},
                        new ProductCardRowMapper());
        if (productCardList.size() == 0)
            throw new NoSuchElementException();
        return productCardList;
    }

    @Override
    public List<ProductCard> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM productcard",
                        new ProductCardRowMapper());
    }

    @Override
    public ProductCard add(ProductCard productCard) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);
        insert.update("INSERT INTO productcard (SKU, AMOUNT, DISLIKES, LIKES, NAME, PRICE, PRODUCTDESCRIPTION, CATEGORY) VALUES(?,?,?,?,?,?,?,?)",
                new Object[]{productCard.getSku(), productCard.getAmount(), productCard.getDislikes(), productCard.getLikes(), productCard.getName(), productCard.getPrice(), productCard.getProductdescription(), productCard.getCategoryId()});
        return get(productCard.getSku());
    }

    @Override
    public ProductCard update(ProductCard productCard) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);
        insert.update("UPDATE productcard SET AMOUNT = ?, DISLIKES = ?, LIKES = ?, NAME = ?, PRICE = ?, PRODUCTDESCRIPTION = ?, CATEGORY = ? WHERE sku = ?",
                new Object[]{productCard.getAmount(), productCard.getDislikes(), productCard.getLikes(), productCard.getName(), productCard.getPrice(), productCard.getProductdescription(), productCard.getCategoryId(), productCard.getSku()});
        return get(productCard.getSku());    }

    @Override
    public void delete(String sku) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM productcard WHERE sku= ?",
                new Object[]{sku});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM productcard");
    }
}
