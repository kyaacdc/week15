package com.smarthouse.dao;

import com.smarthouse.pojo.Visualization;

import java.util.List;

public interface VisualizationDao {

    Visualization get(int id);

    List<Visualization> getAll();

    List<Visualization> getByProductCard(String productCardSku);

    Visualization add(Visualization visualization);

    Visualization update(Visualization visualization);

    void delete(int id);

    void deleteAll();
}
