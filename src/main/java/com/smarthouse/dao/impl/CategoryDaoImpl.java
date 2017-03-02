package com.smarthouse.dao.impl;

import com.smarthouse.dao.CategoryDao;
import com.smarthouse.dao.rowMapper.CategoryRowMapper;
import com.smarthouse.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.NoSuchElementException;

public class CategoryDaoImpl implements CategoryDao{

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Category get(int id) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<Category> categoryList = (List<Category>) select
                .query("SELECT  ID, DESCRIPTION, NAME, CATEGORY FROM category WHERE ID = ?",
                        new Object[]{id},
                        new CategoryRowMapper());
        if (categoryList.size() == 0)
            throw new NoSuchElementException();
        return categoryList.get(0);
    }

    @Override
    public List<Category> getSubCategories(Category category) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<Category> categoryList = (List<Category>) select
                .query("SELECT * FROM category WHERE CATEGORY = ?",
                        new Object[]{category.getId()},
                        new CategoryRowMapper());
        if (categoryList.size() == 0)
            throw new NoSuchElementException();
        return categoryList;
    }

    @Override
    public List<Category> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM category",
                        new CategoryRowMapper());
    }

    @Override
    public Category add(Category category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update(con -> {
            PreparedStatement ps;
            if(category.getCategoryId() == 0){
                ps = con.prepareStatement("INSERT INTO category (DESCRIPTION, NAME) VALUES(?,?)", new String[]{"id"});
                ps.setString(1, category.getDescription());
                ps.setString(2, category.getName());
            }
            else {
                ps = con.prepareStatement("INSERT INTO category (DESCRIPTION, NAME, CATEGORY) VALUES(?,?,?)", new String[]{"id"});
                ps.setString(1, category.getDescription());
                ps.setString(2, category.getName());
                ps.setInt(3, category.getCategoryId());
            }
            return ps;
        }, keyHolder);
        return get(keyHolder.getKey().intValue());
    }

    @Override
    public Category update(Category category) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);
        if(category.getCategoryId() == 0) {
            insert.update("UPDATE category SET DESCRIPTION = ?, NAME = ? WHERE ID = ?",
                    new Object[]{category.getDescription(), category.getName(), category.getId()});
        }
        else {
            insert.update("UPDATE category SET DESCRIPTION = ?, NAME = ?, CATEGORY = ? WHERE ID = ?",
                    new Object[]{category.getDescription(), category.getName(), category.getCategoryId(), category.getId()});
        }
        return get(category.getId());
    }

    @Override
    public void delete(int id) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM category WHERE ID = ?",
                new Object[]{id});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM category");
    }
}
