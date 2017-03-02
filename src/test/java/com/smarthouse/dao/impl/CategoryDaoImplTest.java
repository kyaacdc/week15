package com.smarthouse.dao.impl;

import com.smarthouse.dao.CategoryDao;
import com.smarthouse.pojo.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CategoryDaoImplTest {
    private CategoryDao service;

    @Before
    public void init(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        service = (CategoryDao) ac.getBean("categoryDao");
    }

    @After
    public void after(){
        service = null;
    }

    @Test
    public void testSaveRecord(){
        Category category = new Category("catdesc", "catname", 0);

        //Record to DB
        category = service.add(category);

        assertThat(category.getName(), is(equalTo("catname")));
        assertThat(category.getName(), isA(String.class));
        assertThat(category, is(notNullValue()));
        assertThat(category, is(anything()));
        assertThat(category.getName(), is(greaterThan("")));
        assertThat(category.getName(), is(not(lessThan(""))));
        assertThat(category.getClass(), is(typeCompatibleWith(Category.class)));

        assertThat(category.getDescription(), is(equalTo("catdesc")));
        assertThat(category.getDescription(), isA(String.class));
        assertThat(category.getDescription(), is(greaterThan("")));
        assertThat(category.getDescription(), is(not(lessThan(""))));

        service.delete(category.getId());
    }

    @Test
    public void testDeleteRecord(){
        Category category1 = new Category("catname", "catdesc", 0);

        //Record to DB
        Category category = service.add(category1);
        //Delete from DB
        service.delete(category.getId());
    }

    @Test
    public void testSelect(){
        Category category = new Category("catdesc", "catname", 0);

        //Record to DB
        category = service.add(category);

        //Get from DB
        Category categoryFromDB = service.get(category.getId());

        assertThat(categoryFromDB.getName(), is(equalTo("catname")));
        assertThat(categoryFromDB.getDescription(), is(equalTo("catdesc")));
        assertThat(categoryFromDB.getName(), isA(String.class));
        assertThat(categoryFromDB, is(notNullValue()));
        assertThat(categoryFromDB, is(anything()));
        assertThat(categoryFromDB.getName(), is(greaterThan("")));
        assertThat(categoryFromDB.getName(), is(not(lessThan(""))));
        assertThat(categoryFromDB.getClass(), is(typeCompatibleWith(Category.class)));

        service.delete(category.getId());
    }

    @Test
    public void testSelectRecursive(){
        Category category1 = new Category("catdesc1", "catname1", 0);
        category1 = service.add(category1);
        Category category2 = new Category("catdesc2", "catname21", category1.getId());
        category2 = service.add(category2);
        Category category3 = new Category("catdesc3", "catname31", category1.getId());
        category3 = service.add(category3);
        Category category4 = new Category("catdesc4", "catname42", category2.getId());
        Category category = service.add(category4);


        //Get from DB
        Category categoryFromDB = service.get(category.getId());

        assertThat(categoryFromDB.getName(), is(equalTo("catname42")));
        assertThat(categoryFromDB.getDescription(), is(equalTo("catdesc4")));
        assertThat(categoryFromDB.getName(), isA(String.class));
        assertThat(categoryFromDB, is(notNullValue()));
        assertThat(categoryFromDB, is(anything()));
        assertThat(categoryFromDB.getName(), is(greaterThan("")));
        assertThat(categoryFromDB.getName(), is(not(lessThan(""))));
        assertThat(categoryFromDB.getClass(), is(typeCompatibleWith(Category.class)));

        service.delete(category.getId());
        service.delete(category3.getId());
        service.delete(category2.getId());
        service.delete(category1.getId());
    }

    @Test
    public void testUpdate(){
        Category category1 = new Category("catdesc", "catname", 0);

        //Record to DB
        Category category = service.add(category1);

        assertThat(category.getName(), is(equalTo("catname")));
        assertThat(category.getName(), isA(String.class));
        assertThat(category, is(notNullValue()));
        assertThat(category, is(anything()));
        assertThat(category.getName(), is(greaterThan("")));
        assertThat(category.getName(), is(not(lessThan(""))));
        assertThat(category.getClass(), is(typeCompatibleWith(Category.class)));

        category1 = service.get(category.getId());
        category1.setName("newcatname");

        //Update
        service.update(category1);

        //Get update info
        Category category2 = service.get(category1.getId());

        assertThat(category2.getName(), is(equalTo("newcatname")));
        assertThat(category2.getName(), isA(String.class));
        assertThat(category2, is(notNullValue()));
        assertThat(category2, is(anything()));
        assertThat(category2.getName(), is(greaterThan("")));
        assertThat(category2.getName(), is(not(lessThan(""))));
        assertThat(category2.getClass(), is(typeCompatibleWith(Category.class)));

        service.delete(category2.getId());
    }

    @Test
    public void testGetAll(){
        Category category1 = new Category("1catdesc", "1catname", 0);
        Category category2 = new Category("2catdesc", "2catname", 0);
        Category category3 = new Category("3catdesc", "3catname", 0);

        //Record to DB
        service.add(category1);
        service.add(category2);
        service.add(category3);


        List<Category> categories = service.getAll();

        assertThat(categories, is(notNullValue()));
        assertThat(categories, is(anything()));
        assertThat(categories.get(0), isA(Category.class));

        service.deleteAll();
    }
}
