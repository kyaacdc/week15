package com.smarthouse.dao.impl;

import com.smarthouse.dao.VisualizationDao;
import com.smarthouse.dao.rowMapper.VisualizationRowMapper;
import com.smarthouse.pojo.Visualization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class VisualizationDaoImpl implements VisualizationDao{

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Visualization get(int id) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<Visualization> visualizationList = (List<Visualization>) select
                .query("SELECT  ID, type, URL, PRODUCTCARD FROM visualization WHERE ID = ?",
                        new Object[]{id},
                        new VisualizationRowMapper());
        if (visualizationList.size() == 0)
            throw new NoSuchElementException();
        return visualizationList.get(0);
    }

    @Override
    public List<Visualization> getAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("SELECT * FROM visualization",
                        new VisualizationRowMapper());
    }

    @Override
    public List<Visualization> getByProductCard(String productCardSku) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<Visualization> visualizationList = (List<Visualization>) select
                .query("SELECT  * FROM visualization WHERE PRODUCTCARD = ?",
                        new Object[]{productCardSku},
                        new VisualizationRowMapper());
        if (visualizationList.size() == 0)
            throw new NoSuchElementException();
        return visualizationList;
    }

    @Override
    public Visualization add(Visualization visualization) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update(con -> {
            PreparedStatement ps;
            if(visualization.getProductCardSku() == null){
                ps = con.prepareStatement("INSERT INTO visualization (type, URL) VALUES(?,?)", new String[]{"id"});
                ps.setInt(1, visualization.getType());
                ps.setString(2, visualization.getUrl());
            }
            else {
                ps = con.prepareStatement("INSERT INTO visualization (type, URL, PRODUCTCARD) VALUES(?,?,?)", new String[]{"id"});
                ps.setInt(1, visualization.getType());
                ps.setString(2, visualization.getUrl());
                ps.setString(3, visualization.getProductCardSku());
            }
            return ps;
        }, keyHolder);
        return get(keyHolder.getKey().intValue());
    }

    @Override
    public Visualization update(Visualization visualization) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update("UPDATE visualization SET type = ?, URL = ?, PRODUCTCARD = ? WHERE ID = ?",
                new Object[]{visualization.getType(), visualization.getUrl(), visualization.getProductCardSku(), visualization.getId()});

        return get(visualization.getId());
    }

    @Override
    public void delete(int id) {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM visualization WHERE ID = ?",
                new Object[]{id});
    }

    @Override
    public void deleteAll() {
        JdbcTemplate delete = new JdbcTemplate(dataSource);
        delete.update("DELETE FROM visualization");
    }
}
