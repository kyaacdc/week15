package com.smarthouse.dao;

import com.smarthouse.pojo.Category;
import com.smarthouse.pojo.ProductCard;
import com.smarthouse.util.DbCreator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/app-config.xml")
public class ProductCardDaoTest {

    @Resource
    private ProductCardDao service;
    @Resource
    private CategoryDao categoryDao;

    @BeforeClass
    public static void dropCreateDb() throws SQLException, InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        DbCreator dbCreator = (DbCreator) ac.getBean("dbCreator");
        dbCreator.dropCreateDbAndTables();
    }

    @Test
    public void testSaveRecord() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        ProductCard productCard = service.add(productCard1);

        assertThat(productCard.getAmount(), is(equalTo(34)));
        assertThat(productCard.getPrice(), is(equalTo(2222)));
        assertThat(productCard.getLikes(), is(equalTo(45)));
        assertThat(productCard.getDislikes(), is(equalTo(4)));
        assertThat(productCard.getAmount(), isA(Integer.class));
        assertThat(productCard, is(notNullValue()));
        assertThat(productCard, is(anything()));
        assertThat(productCard.getPrice(), is(greaterThan(2000)));
        assertThat(productCard.getAmount(), is(not(lessThan(4))));
        assertThat(productCard.getClass(), is(typeCompatibleWith(ProductCard.class)));

        service.delete("888");
        categoryDao.delete(category.getId());
    }

    @Test
    public void testDeleteRecord() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        ProductCard productCard = service.add(productCard1);

        //Delete records
        service.delete("888");
        categoryDao.delete(category.getId());
    }

    @Test
    public void testSelect() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        ProductCard productCard = service.add(productCard1);

        //Get from DB
        ProductCard productCardFromDB = service.get(productCard.getSku());

        assertThat(productCardFromDB.getPrice(), is(equalTo(2222)));
        assertThat(productCardFromDB.getPrice(), isA(Integer.class));
        assertThat(productCardFromDB, is(notNullValue()));
        assertThat(productCardFromDB, is(anything()));
        assertThat(productCardFromDB.getClass(), is(typeCompatibleWith(ProductCard.class)));


        //Delete records
        service.delete(productCardFromDB.getSku());
        categoryDao.delete(category.getId());
    }

    @Test
    public void testUpdate() {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        ProductCard productCard = service.add(productCard1);

        assertThat(productCard.getAmount(), is(equalTo(34)));
        assertThat(productCard.getPrice(), is(equalTo(2222)));
        assertThat(productCard.getLikes(), is(equalTo(45)));
        assertThat(productCard.getDislikes(), is(equalTo(4)));
        assertThat(productCard.getAmount(), isA(Integer.class));
        assertThat(productCard, is(notNullValue()));
        assertThat(productCard, is(anything()));
        assertThat(productCard.getPrice(), is(greaterThan(2000)));
        assertThat(productCard.getAmount(), is(not(lessThan(4))));
        assertThat(productCard.getClass(), is(typeCompatibleWith(ProductCard.class)));

        productCard1.setAmount(6);

        //Update
        service.update(productCard1);

        assertThat(service.get("888").getAmount(), is(equalTo(6)));

        service.delete("888");
        categoryDao.delete(category.getId());
    }

    @Test
    public void testGetAll(){
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "3name", 3333, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard3 = new ProductCard("000", "4name", 444, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCard1 = service.add(productCard1);
        productCard2 = service.add(productCard2);
        productCard3 = service.add(productCard3);

        List<ProductCard> productCards = service.getAll();

        assertThat(productCard1.getName(), is (equalTo("2name")));
        assertThat(productCard2.getName(), is (equalTo("3name")));
        assertThat(productCard3.getName(), is (equalTo("4name")));
        assertThat(productCards, is(notNullValue()));
        assertThat(productCards, is(anything()));
        assertThat(productCards.get(1), isA(ProductCard.class));

        service.delete("888");
        service.delete("999");
        service.delete("000");

        categoryDao.delete(category.getId());
    }

    @Test
    public void testGetByCategory(){
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "3name", 3333, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard3 = new ProductCard("000", "4name", 444, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCard1 = service.add(productCard1);
        productCard2 = service.add(productCard2);
        productCard3 = service.add(productCard3);

        List<ProductCard> productCards = service.getByCategory(category.getId());

        assertThat(productCard1.getName(), is (equalTo("2name")));
        assertThat(productCard2.getName(), is (equalTo("3name")));
        assertThat(productCard3.getName(), is (equalTo("4name")));
        assertThat(productCards, is(notNullValue()));
        assertThat(productCards, is(anything()));
        assertThat(productCards.get(1), isA(ProductCard.class));

        service.delete("888");
        service.delete("999");
        service.delete("000");

        categoryDao.delete(category.getId());
    }
}


