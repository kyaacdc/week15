package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.Category;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<Category> extractor = rs1 -> {
            Category category = new Category();
            category.setId(rs1.getInt(1));
            category.setDescription(rs1.getString(2));
            category.setName(rs1.getString(3));
            category.setCategoryId(rs1.getInt(4));
            return category;
        };

        return extractor.extractData(rs);
    }
}
