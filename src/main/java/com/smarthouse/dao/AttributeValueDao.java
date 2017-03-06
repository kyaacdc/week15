package com.smarthouse.dao;

import com.smarthouse.dao.rowMapper.AttributeValueRowMapper;
import com.smarthouse.pojo.AttributeValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class AttributeValueDao extends MajorDao{
    
    public AttributeValue get(int id) {
        return getJdbcTemplate()
                .queryForObject("SELECT * FROM attributevalue WHERE ID = ?",
                        new Object[]{id},
                        new AttributeValueRowMapper());
    }

    public List<AttributeValue> getAll() {
        return getJdbcTemplate()
                .query("SELECT * FROM attributevalue",
                        new AttributeValueRowMapper());
    }

    public List<AttributeValue> getByProductCard(String productCardSku) {
        List<AttributeValue> attributeValueList = (List<AttributeValue>) getJdbcTemplate()
                .query("SELECT  * FROM attributevalue WHERE PRODUCTCARD = ?",
                        new Object[]{productCardSku},
                        new AttributeValueRowMapper());
        if (attributeValueList.size() == 0)
            throw new NoSuchElementException();
        return attributeValueList;
    }

    public List<AttributeValue> getByAttributeName(String attributeName) {
        List<AttributeValue> attributeValueList = (List<AttributeValue>) getJdbcTemplate()
                .query("SELECT  * FROM attributevalue WHERE ATTRIBUTENAME = ?",
                        new Object[]{attributeName},
                        new AttributeValueRowMapper());
        if (attributeValueList.size() == 0)
            throw new NoSuchElementException();
        return attributeValueList;
    }

    public AttributeValue add(AttributeValue attributeValue) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(con -> {
            PreparedStatement ps;
            if(attributeValue.getAttributeName() == null){
                ps = con.prepareStatement("INSERT INTO attributevalue (VALUE, PRODUCTCARD) VALUES(?,?)", new String[]{"id"});
                ps.setString(1, attributeValue.getValue());
                ps.setString(2, attributeValue.getProductCardSku());
            }
            else if(attributeValue.getProductCardSku() == null){
                ps = con.prepareStatement("INSERT INTO attributevalue (VALUE , ATTRIBUTENAME) VALUES(?,?)", new String[]{"id"});
                ps.setString(1, attributeValue.getValue());
                ps.setString(2, attributeValue.getAttributeName());
            }
            else {
                ps = con.prepareStatement("INSERT INTO attributevalue (VALUE , ATTRIBUTENAME, PRODUCTCARD) VALUES(?,?,?)", new String[]{"id"});
                ps.setString(1, attributeValue.getValue());
                ps.setString(2, attributeValue.getAttributeName());
                ps.setString(3, attributeValue.getProductCardSku());
            }
            return ps;
        }, keyHolder);
        return get(keyHolder.getKey().intValue());
    }

    public AttributeValue update(AttributeValue attributeValue) {
        getJdbcTemplate().update("UPDATE attributevalue SET VALUE = ?, ATTRIBUTENAME = ?, PRODUCTCARD = ? WHERE ID = ?",
                attributeValue.getValue(), attributeValue.getAttributeName(), attributeValue.getProductCardSku(), attributeValue.getId());
        return get(attributeValue.getId());
    }

    public void delete(int id) {
        getJdbcTemplate().update("DELETE FROM attributevalue WHERE ID = ?", id);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM attributevalue");
    }
}
