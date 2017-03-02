package com.smarthouse.dao.impl;

import com.smarthouse.dao.CategoryDao;
import com.smarthouse.dao.ProductCardDao;
import com.smarthouse.dao.VisualizationDao;
import com.smarthouse.pojo.Category;
import com.smarthouse.pojo.ProductCard;
import com.smarthouse.pojo.Visualization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/app-config.xml")
public class VisualizationDaoImplTest {

    @Resource
    private VisualizationDao service;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private ProductCardDao productCardDao;

    @Test
    public void testSaveRecord() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        Visualization visualization = new Visualization(2, "url", productCard.getSku());

        visualization.setId(1);

        //Record to DB
        visualization = service.add(visualization);

        assertThat(visualization.getUrl(), is(equalTo("url")));
        assertThat(visualization.getType(), is(equalTo(2)));
        assertThat(visualization.getProductCardSku(), is(equalTo("222")));
        assertThat(categoryDao.get(productCardDao.get(visualization.getProductCardSku()).getCategoryId()).getName(), is(equalTo("name")));
        assertThat(visualization.getType(), isA(Integer.class));
        assertThat(visualization, is(notNullValue()));
        assertThat(visualization, is(anything()));
        assertThat(visualization.getClass(), is(typeCompatibleWith(Visualization.class)));

        service.delete(visualization.getId());
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
    }

    @Test
    public void testDeleteRecord() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        Visualization visualization = new Visualization(2, "url", productCard.getSku());
        visualization.setId(1);

        //Record to DB
        visualization = service.add(visualization);

        //Delete from DB
        service.delete(visualization.getId());
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
    }


    @Test
    public void testSelect() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        Visualization visualization = new Visualization(2, "url", productCard.getSku());
        visualization.setId(1);

        //Record to DB
        visualization = service.add(visualization);
        //Get from DB
        Visualization visualizationFromDB = service.get(visualization.getId());

        assertThat(visualizationFromDB.getUrl(), is(equalTo("url")));
        assertThat(visualizationFromDB.getType(), is(equalTo(2)));
        assertThat(visualizationFromDB.getProductCardSku(), is(equalTo("222")));
        assertThat(categoryDao.get(productCardDao.get(visualizationFromDB.getProductCardSku()).getCategoryId()).getName(), is(equalTo("name")));
        assertThat(visualizationFromDB.getType(), isA(Integer.class));
        assertThat(visualizationFromDB, is(notNullValue()));
        assertThat(visualizationFromDB, is(anything()));
        assertThat(visualization.getClass(), is(typeCompatibleWith(Visualization.class)));

        //Delete from DB
        service.delete(visualization.getId());
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
    }

    @Test
    public void testUpdate() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        Visualization visualization = new Visualization(2, "url", productCard.getSku());
        //Record to DB
        visualization = service.add(visualization);

        assertThat(visualization.getUrl(), is(equalTo("url")));
        assertThat(visualization.getType(), is(equalTo(2)));
        assertThat(visualization.getProductCardSku(), is(equalTo("222")));
        assertThat(categoryDao.get(productCardDao.get(visualization.getProductCardSku()).getCategoryId()).getName(), is(equalTo("name")));
        assertThat(visualization.getType(), isA(Integer.class));
        assertThat(visualization, is(notNullValue()));
        assertThat(visualization, is(anything()));
        assertThat(visualization.getClass(), is(typeCompatibleWith(Visualization.class)));

        visualization.setUrl("newUrl");

        //Update
        service.update(visualization);

        //Get update info
        visualization = service.get(visualization.getId());

        assertThat(visualization.getUrl(), is(equalTo("newUrl")));

        //Delete from DB
        service.delete(visualization.getId());
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
    }

    @Test
    public void testGetAll(){
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        //Record to DB
        Visualization visualization1 = new Visualization(2, "1url", productCard.getSku());
        Visualization visualization2 = new Visualization(4, "2url", productCard.getSku());
        Visualization visualization3 = new Visualization(6, "3url", productCard.getSku());

        visualization1 = service.add(visualization1);
        visualization2 = service.add(visualization2);
        visualization3 = service.add(visualization3);

        List<Visualization> visualizations = service.getAll();

        assertThat(visualizations, is(notNullValue()));
        assertThat(visualizations, is(anything()));
        assertThat(visualizations.get(0), isA(Visualization.class));
        assertThat(visualization1.getUrl(), is(equalTo("1url")));
        assertThat(visualization2.getType(), is(equalTo(4)));
        assertThat(visualization3.getUrl(), is(equalTo("3url")));

        //Delete from DB
        service.deleteAll();
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
    }

    @Test
    public void testGetByProductCard(){
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        //Record to DB
        Visualization visualization1 = new Visualization(2, "1url", productCard.getSku());
        Visualization visualization2 = new Visualization(4, "2url", productCard.getSku());
        Visualization visualization3 = new Visualization(6, "3url", productCard.getSku());

        visualization1 = service.add(visualization1);
        visualization2 = service.add(visualization2);
        visualization3 = service.add(visualization3);

        List<Visualization> visualizations = service.getByProductCard("222");

        assertThat(visualizations, is(notNullValue()));
        assertThat(visualizations, is(anything()));
        assertThat(visualizations.get(0), isA(Visualization.class));
        assertThat(visualization1.getUrl(), is(equalTo("1url")));
        assertThat(visualization2.getType(), is(equalTo(4)));
        assertThat(visualization3.getUrl(), is(equalTo("3url")));

        //Delete from DB
        service.deleteAll();
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
    }
}
