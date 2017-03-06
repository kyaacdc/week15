package com.smarthouse.dao;

import com.smarthouse.dao.rowMapper.AttributeNameRowMapper;
import com.smarthouse.pojo.AttributeName;
import java.util.List;

public class AttributeNameDao extends MajorDao{

    public AttributeName get(String name) {
        return getJdbcTemplate()
                .queryForObject("SELECT * FROM attributename WHERE name = ?",
                        new Object[]{name},
                        new AttributeNameRowMapper());
    }

    public List<AttributeName> getAll() {
        return getJdbcTemplate()
                .query("SELECT * FROM attributename",
                        new AttributeNameRowMapper());
    }

    public AttributeName add(AttributeName attributeName) {
        getJdbcTemplate().update("INSERT INTO attributename (NAME) VALUES(?)",
                attributeName.getName());
        return get(attributeName.getName());
    }

    public void delete(String name) {
        getJdbcTemplate().update("DELETE FROM attributename WHERE NAME = ?", name);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM attributename");
    }
}
