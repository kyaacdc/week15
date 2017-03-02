package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.ProductCard;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductCardRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<ProductCard> extractor = rs1 -> {
            ProductCard productCard = new ProductCard();
            productCard.setSku(rs1.getString(1));
            productCard.setAmount(rs1.getInt(2));
            productCard.setDislikes(rs1.getInt(3));
            productCard.setLikes(rs1.getInt(4));
            productCard.setName(rs1.getString(5));
            productCard.setPrice(rs1.getInt(6));
            productCard.setProductDescription(rs1.getString(7));
            productCard.setCategoryId(rs1.getInt(8));

            return productCard;
        };

        return extractor.extractData(rs);
    }
}
