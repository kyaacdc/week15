package com.smarthouse.dao;

import com.smarthouse.dao.rowMapper.VisualizationRowMapper;
import com.smarthouse.pojo.Visualization;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;

public class VisualizationDao extends MajorDao{

    public Visualization get(int id) {
        return getJdbcTemplate()
                .queryForObject("SELECT  ID, type, URL, PRODUCTCARD FROM visualization WHERE ID = ?",
                        new Object[]{id},
                        new VisualizationRowMapper());
    }

    public List<Visualization> getAll() {
        return getJdbcTemplate()
                .query("SELECT * FROM visualization",
                        new VisualizationRowMapper());
    }

    public List<Visualization> getByProductCard(String productCardSku) {
        List<Visualization> visualizationList = (List<Visualization>) getJdbcTemplate()
                .query("SELECT  * FROM visualization WHERE PRODUCTCARD = ?",
                        new Object[]{productCardSku},
                        new VisualizationRowMapper());
        if (visualizationList.size() == 0)
            throw new NoSuchElementException();
        return visualizationList;
    }

    public Visualization add(Visualization visualization) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(con -> {
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

    public Visualization update(Visualization visualization) {

        getJdbcTemplate().update("UPDATE visualization SET type = ?, URL = ?, PRODUCTCARD = ? WHERE ID = ?",
                visualization.getType(), visualization.getUrl(), visualization.getProductCardSku(), visualization.getId());

        return get(visualization.getId());
    }

    public void delete(int id) {
        getJdbcTemplate().update("DELETE FROM visualization WHERE ID = ?", id);
    }

    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM visualization");
    }
}
