package com.smarthouse.dao.rowMapper;

import com.smarthouse.pojo.Visualization;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VisualizationRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetExtractor<Visualization> extractor = rs1 -> {
            Visualization visualization = new Visualization();
            visualization.setId(rs1.getInt(1));
            visualization.setType(rs1.getInt(2));
            visualization.setUrl(rs1.getString(3));
            visualization.setProductCardSku(rs1.getString(4));
            return visualization;
        };

        return extractor.extractData(rs);
    }
}
