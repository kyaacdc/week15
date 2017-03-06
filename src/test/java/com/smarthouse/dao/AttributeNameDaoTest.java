package com.smarthouse.dao;

import com.smarthouse.pojo.AttributeName;
import com.smarthouse.util.DbCreator;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/app-config.xml")
public class AttributeNameDaoTest {

    @Resource
    private AttributeNameDao service;

    @BeforeClass
    public static void dropCreateDb() throws SQLException, InterruptedException, IOException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        DbCreator dbCreator = (DbCreator) ac.getBean("dbCreator");
        dbCreator.dropCreateDbAndTables();
    }

    @Test
    public void testSaveRecord(){
        AttributeName attributeName1 = new AttributeName();
        attributeName1.setName("temp");

        //Record to DB
        AttributeName attributeName = service.add(attributeName1);

        assertThat(attributeName.getName(), is(equalTo("temp")));
        assertThat(attributeName.getName(), isA(String.class));
        assertThat(attributeName, is(notNullValue()));
        assertThat(attributeName, is(anything()));
        assertThat(attributeName.getName(), is(greaterThan("")));
        assertThat(attributeName.getName(), is(not(lessThan(""))));
        assertThat(attributeName.getClass(), is(typeCompatibleWith(AttributeName.class)));

        service.delete("temp");

    }

    @Test
    public void testDeleteRecord(){
        //Create attribute
        AttributeName attributeName1 = new AttributeName();
        attributeName1.setName("weight");

        //Record to DB
        AttributeName attributeName = service.add(attributeName1);

        //Delete from DB
        service.delete(attributeName.getName());
    }

    @Test
    public void testSelect(){
        //Create attribute
        AttributeName attributeName1 = new AttributeName();
        attributeName1.setName("temp");

        //Record to DB
        AttributeName attributeName = service.add(attributeName1);

        //Get from DB
        AttributeName cityFromDB = service.get(attributeName.getName());

        assertThat(cityFromDB.getName(), is(equalTo("temp")));
        assertThat(attributeName.getName(), isA(String.class));
        assertThat(attributeName, is(notNullValue()));
        assertThat(attributeName, is(anything()));
        assertThat(attributeName.getName(), is(greaterThan("")));
        assertThat(attributeName.getName(), is(not(lessThan(""))));
        assertThat(attributeName.getClass(), is(typeCompatibleWith(AttributeName.class)));

        service.delete("temp");
    }

    @Test
    public void testGetAll(){
        AttributeName attributeName1 = new AttributeName();
        attributeName1.setName("temp1");
        AttributeName attributeName2 = new AttributeName();
        attributeName2.setName("temp2");
        AttributeName attributeName3 = new AttributeName();
        attributeName3.setName("temp3");

        attributeName1 = service.add(attributeName1);
        attributeName2 = service.add(attributeName2);
        attributeName3 = service.add(attributeName3);

        List<AttributeName> attributeNames = service.getAll();

        assertThat(attributeName1.getName(), is(equalTo("temp1")));
        assertThat(attributeName2.getName(), is(equalTo("temp2")));
        assertThat(attributeName3.getName(), is(equalTo("temp3")));
        assertThat(attributeNames, is(notNullValue()));
        assertThat(attributeNames, is(anything()));
        assertThat(attributeNames.get(0), isA(AttributeName.class));

        service.delete("temp1");
        service.delete("temp2");
        service.delete("temp3");
    }

}