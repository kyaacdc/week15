package com.smarthouse.dao.impl;

import com.smarthouse.dao.AttributeValueDao;
import com.smarthouse.dao.rowMapper.AttributeValueRowMapper;
import com.smarthouse.pojo.AttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class AttributeValueDaoImpl implements AttributeValueDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AttributeValue get(int id) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<AttributeValue> attributeValueList = (List<AttributeValue>) select
                .query("SELECT * FROM attributevalue WHERE ID = ?",
                        new Object[]{id},
                        new AttributeValueRowMapper());
        if (attributeValueList.size() == 0)
            throw new NoSuchElementException();
        return attributeValueList.get(0);
    }

    @Override
    public List<AttributeValue> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM attributevalue",
                        new AttributeValueRowMapper());
    }

    @Override
    public List<AttributeValue> getByProductCard(String productCardSku) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<AttributeValue> attributeValueList = (List<AttributeValue>) select
                .query("SELECT  * FROM attributevalue WHERE PRODUCTCARD = ?",
                        new Object[]{productCardSku},
                        new AttributeValueRowMapper());
        if (attributeValueList.size() == 0)
            throw new NoSuchElementException();
        return attributeValueList;
    }

    @Override
    public List<AttributeValue> getByAttributeName(String attributeName) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<AttributeValue> attributeValueList = (List<AttributeValue>) select
                .query("SELECT  * FROM attributevalue WHERE ATTRIBUTENAME = ?",
                        new Object[]{attributeName},
                        new AttributeValueRowMapper());
        if (attributeValueList.size() == 0)
            throw new NoSuchElementException();
        return attributeValueList;
    }

    @Override
    public AttributeValue add(AttributeValue attributeValue) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update(con -> {
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

    @Override
    public AttributeValue update(AttributeValue attributeValue) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update("UPDATE attributevalue SET VALUE = ?, ATTRIBUTENAME = ?, PRODUCTCARD = ? WHERE ID = ?",
                new Object[]{attributeValue.getValue(), attributeValue.getAttributeName(), attributeValue.getProductCardSku(), attributeValue.getId()});

        return get(attributeValue.getId());
    }

    @Override
    public void delete(int id) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM attributevalue WHERE ID = ?",
                new Object[]{id});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM attributevalue");
    }
}
