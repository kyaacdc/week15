package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.AttributeValue;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttributeValueRowMapper  implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<AttributeValue> extractor = rs1 -> {
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setId(rs1.getInt(1));
            attributeValue.setValue(rs1.getString(2));
            attributeValue.setAttributeName(rs1.getString(3));
            attributeValue.setProductCardSku(rs1.getString(4));
            return attributeValue;
        };

        return extractor.extractData(rs);
    }
}
