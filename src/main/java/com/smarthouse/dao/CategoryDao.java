package com.smarthouse.dao;

import com.smarthouse.dao.rowMapper.CategoryRowMapper;
import com.smarthouse.pojo.Category;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class CategoryDao extends MajorDao{

    public Category get(int id) {
        return getJdbcTemplate()
                .queryForObject("SELECT  ID, DESCRIPTION, NAME, CATEGORY FROM category WHERE ID = ?",
                        new Object[]{id},
                        new CategoryRowMapper());
    }

    public List<Category> getSubCategories(Category category) {
        List<Category> categoryList = (List<Category>) getJdbcTemplate()
                .query("SELECT * FROM category WHERE CATEGORY = ?",
                        new Object[]{category.getId()},
                        new CategoryRowMapper());
        if (categoryList.size() == 0)
            throw new NoSuchElementException();
        return categoryList;
    }

    public List<Category> getAll() {
        return getJdbcTemplate()
                .query("SELECT * FROM category",
                        new CategoryRowMapper());
    }

    public Category add(Category category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(con -> {
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

    public Category update(Category category) {
        if(category.getCategoryId() == 0) {
            getJdbcTemplate().update("UPDATE category SET DESCRIPTION = ?, NAME = ? WHERE ID = ?",
                    category.getDescription(), category.getName(), category.getId());
        }
        else {
            getJdbcTemplate().update("UPDATE category SET DESCRIPTION = ?, NAME = ?, CATEGORY = ? WHERE ID = ?",
                    category.getDescription(), category.getName(), category.getCategoryId(), category.getId());
        }
        return get(category.getId());
    }

    public void delete(int id) {
        getJdbcTemplate().update("DELETE FROM category WHERE ID = ?", id);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM category");
    }
}
