package com.smarthouse.dao;

import com.smarthouse.pojo.AttributeName;
import com.smarthouse.pojo.AttributeValue;
import com.smarthouse.pojo.Category;
import com.smarthouse.pojo.ProductCard;
import com.smarthouse.util.DbCreator;
import org.junit.*;
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
public class AttributeValueDaoTest {

    @Resource
    private AttributeValueDao serviceAttrVal;
    @Resource
    private AttributeNameDao attributeNameDao;
    @Resource
    private ProductCardDao productCardDao;
    @Resource
    private CategoryDao categoryDao;

    @BeforeClass
    public static void dropCreateDb() throws SQLException, InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        DbCreator dbCreator = (DbCreator) ac.getBean("dbCreator");
        dbCreator.dropCreateDbAndTables();
    }

    @Before
    public void init(){
        attributeNameDao.add(new AttributeName("color"));
        Category category = categoryDao.add(new Category("desc", "name", 0));
        productCardDao.add(new ProductCard("111", "name", 123, 1, 1, 1, "decs", category.getId()));

    }

    @After
    public void after(){
        serviceAttrVal = null;
        attributeNameDao.delete("color");
        productCardDao.delete("111");
        categoryDao.deleteAll();
    }

    @Test
    public void testSaveRecord(){
        AttributeValue attributeValue = new AttributeValue("white", "color", "111");
        attributeValue = serviceAttrVal.add(attributeValue);

        assertThat(attributeValue.getValue(), equalTo("white"));
        assertThat(attributeValue.getAttributeName(), equalTo("color"));
        assertThat(attributeValue.getProductCardSku(), equalTo("111"));

        serviceAttrVal.delete(attributeValue.getId());
    }

    @Test
    public void testDeleteRecord(){
        AttributeValue attributeValue = new AttributeValue("white", "color", "111");
        attributeValue = serviceAttrVal.add(attributeValue);
        serviceAttrVal.delete(attributeValue.getId());
    }

    @Test
    public void testSelect(){
        AttributeValue attributeValue = new AttributeValue("white", "color", "111");
        attributeValue = serviceAttrVal.add(attributeValue);

        attributeValue = serviceAttrVal.get(attributeValue.getId());

        assertThat(attributeValue.getValue(), equalTo("white"));
        assertThat(attributeValue.getAttributeName(), equalTo("color"));
        assertThat(attributeValue.getProductCardSku(), equalTo("111"));
        assertThat(attributeValue.getValue(), isA(String.class));
        assertThat(attributeValue.getValue(), is(notNullValue()));
        assertThat(attributeValue, is(anything()));
        assertThat(attributeValue.getValue(), is(greaterThan("")));
        assertThat(attributeValue.getValue(), is(not(lessThan(""))));
        assertThat(attributeValue.getClass(), is(typeCompatibleWith(AttributeValue.class)));

        serviceAttrVal.delete(attributeValue.getId());

    }

    @Test
    public void testUpdate(){
        AttributeValue attributeValue = new AttributeValue("white", "color", "111");
        attributeValue = serviceAttrVal.add(attributeValue);

        attributeValue = serviceAttrVal.get(attributeValue.getId());

        assertThat(attributeValue.getValue(), equalTo("white"));
        assertThat(attributeValue.getAttributeName(), equalTo("color"));
        assertThat(attributeValue.getProductCardSku(), equalTo("111"));
        assertThat(attributeValue.getValue(), isA(String.class));
        assertThat(attributeValue.getValue(), is(notNullValue()));
        assertThat(attributeValue, is(anything()));
        assertThat(attributeValue.getValue(), is(greaterThan("")));
        assertThat(attributeValue.getValue(), is(not(lessThan(""))));
        assertThat(attributeValue.getClass(), is(typeCompatibleWith(AttributeValue.class)));

        attributeValue.setValue("red");

        serviceAttrVal.update(attributeValue);

        assertThat(attributeValue.getValue(), equalTo("red"));


        serviceAttrVal.delete(attributeValue.getId());
    }

    @Test
    public void testGetAll(){

        AttributeValue attributeValue = new AttributeValue("white", "color", "111");
        serviceAttrVal.add(attributeValue);

        attributeValue = new AttributeValue("black", "color", "111");
        serviceAttrVal.add(attributeValue);

        attributeValue = new AttributeValue("blue", "color", "111");
        serviceAttrVal.add(attributeValue);

        List<AttributeValue> attributeValues = serviceAttrVal.getAll();

        for(AttributeValue a: attributeValues) {
            assertThat(a, is(notNullValue()));
            assertThat(a, is(anything()));
            assertThat(a.getValue(), oneOf("white", "black", "blue"));
            assertThat(a.getValue(), isA(String.class));
        }

        serviceAttrVal.deleteAll();
    }

    @Test
    public void testGetByProductCard(){
        AttributeValue attributeValue = new AttributeValue("white", "color", "111");
        serviceAttrVal.add(attributeValue);

        List<AttributeValue> list = serviceAttrVal.getByProductCard("111");

        assertThat(list.get(0).getValue(), is(equalTo("white")));

        serviceAttrVal.delete(list.get(0).getId());
    }

    @Test
    public void testGetByAttributeName(){
        AttributeValue attributeValue = new AttributeValue("white", "color", "111");
        serviceAttrVal.add(attributeValue);

        List<AttributeValue> list = serviceAttrVal.getByAttributeName("color");

        assertThat(list.get(0).getValue(), is(equalTo("white")));

        serviceAttrVal.delete(list.get(0).getId());
    }
}