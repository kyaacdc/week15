package com.smarthouse.dao;

import com.smarthouse.pojo.ProductCard;
import com.smarthouse.dao.rowMapper.ProductCardRowMapper;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductCardDao extends MajorDao{
    
    public ProductCard get(String sku) {
         return getJdbcTemplate()
                 .queryForObject("SELECT  SKU, AMOUNT, DISLIKES, LIKES, NAME, PRICE, PRODUCTDESCRIPTION, CATEGORY FROM productcard WHERE sku = ?",
                         new Object[]{sku},
                         new ProductCardRowMapper());
    }

    public List<ProductCard> getByCategory(int categoryId) {
        List<ProductCard> productCardList = (List<ProductCard>) getJdbcTemplate()
                .query("SELECT  * FROM productcard WHERE CATEGORY = ?",
                        new Object[]{categoryId},
                        new ProductCardRowMapper());
        if (productCardList.size() == 0)
            throw new NoSuchElementException();
        return productCardList;
    }

    public List<ProductCard> getAll() {
        return getJdbcTemplate()
                .query("SELECT * FROM productcard",
                        new ProductCardRowMapper());
    }

    public ProductCard add(ProductCard productCard) {
        getJdbcTemplate().update("INSERT INTO productcard (SKU, AMOUNT, DISLIKES, LIKES, NAME, PRICE, PRODUCTDESCRIPTION, CATEGORY) VALUES(?,?,?,?,?,?,?,?)",
                productCard.getSku(), productCard.getAmount(), productCard.getDislikes(), productCard.getLikes(), productCard.getName(), productCard.getPrice(), productCard.getProductDescription(), productCard.getCategoryId());
        return get(productCard.getSku());
    }

    public ProductCard update(ProductCard productCard) {
        getJdbcTemplate().update("UPDATE productcard SET AMOUNT = ?, DISLIKES = ?, LIKES = ?, NAME = ?, PRICE = ?, PRODUCTDESCRIPTION = ?, CATEGORY = ? WHERE sku = ?",
                productCard.getAmount(), productCard.getDislikes(), productCard.getLikes(), productCard.getName(), productCard.getPrice(), productCard.getProductDescription(), productCard.getCategoryId(), productCard.getSku());
        return get(productCard.getSku());    }

    public void delete(String sku) {
        getJdbcTemplate().update("DELETE FROM productcard WHERE sku= ?", sku);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM productcard");
    }
}
