package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.AttributeName;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttributeNameRowMapper implements RowMapper<AttributeName> {
    @Override
    public AttributeName mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<AttributeName> extractor = rs1 -> {
            AttributeName attributeName = new AttributeName();
            attributeName.setName(rs1.getString(1));
            return attributeName;
        };

        return extractor.extractData(rs);
    }
}
