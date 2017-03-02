package com.smarthouse.dao.impl;

import com.smarthouse.dao.AttributeNameDao;
import com.smarthouse.dao.rowMapper.AttributeNameRowMapper;
import com.smarthouse.pojo.AttributeName;
import com.smarthouse.pojo.validators.Name;
import com.smarthouse.pojo.validators.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class AttributeNameDaoImpl implements AttributeNameDao{

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AttributeName get(String name) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<AttributeName> attributeNameList = (List<AttributeName>) select
                .query("SELECT * FROM attributename WHERE name = ?",
                        new Object[]{name},
                        new AttributeNameRowMapper());
        if (attributeNameList.size() == 0)
            throw new NoSuchElementException();
        return attributeNameList.get(0);
    }

    @Override
    public List<AttributeName> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM attributename",
                        new AttributeNameRowMapper());
    }

    @Override
    public AttributeName add(AttributeName attributeName) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);
        insert.update("INSERT INTO attributename (NAME) VALUES(?)",
                new Object[]{attributeName.getName()});
        return get(attributeName.getName());
    }

    @Override
    public void delete(String name) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM attributename WHERE NAME = ?",
                new Object[]{name});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM attributename");
    }
}
