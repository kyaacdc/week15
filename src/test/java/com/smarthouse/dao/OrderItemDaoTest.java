package com.smarthouse.dao;

import com.smarthouse.pojo.*;
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
public class OrderItemDaoTest {

    @Resource
    private OrderItemDao service;
    @Resource
    private ProductCardDao productCardDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private OrderMainDao orderMainDao;
    @Resource
    private CustomerDao customerDao;

    private OrderMain orderMain;

    @BeforeClass
    public static void dropCreateDb() throws SQLException, InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        DbCreator dbCreator = (DbCreator) ac.getBean("dbCreator");
        dbCreator.dropCreateDbAndTables();
    }


    @Before
    public void init() {
        Category category = categoryDao.add(new Category("desc", "name", 0));
        productCardDao.add(new ProductCard("111", "name", 123, 1, 1, 1, "decs", category.getId()));
        Customer customer = customerDao.add(new Customer("anniya@bk.ru", "Yuriy", false, "7585885"));
        orderMain = orderMainDao.add(new OrderMain("OrderAddress", 1, customer));
    }

    @After
    public void after() {
        service.deleteAll();
        productCardDao.deleteAll();
        categoryDao.deleteAll();
        orderMainDao.deleteAll();
        customerDao.deleteAll();
    }

    @Test
    public void testSaveRecord() {

        OrderItem orderItem = service.add(new OrderItem(5,555, "111", orderMain.getOrderId()));

        assertThat(orderItem.getAmount(), is(equalTo(5)));
        assertThat(orderItem.getTotalprice(), is(equalTo(555)));
        assertThat(orderItem.getAmount(), isA(Integer.class));
        assertThat(orderItem, is(notNullValue()));
        assertThat(orderItem, is(anything()));
        assertThat(orderItem.getTotalprice(), is(greaterThan(554)));
        assertThat(orderItem.getAmount(), is(not(lessThan(4))));
        assertThat(orderItem.getClass(), is(typeCompatibleWith(OrderItem.class)));
    }

    @Test
    public void testDeleteRecord() {
        OrderItem orderItem = service.add(new OrderItem(5,555, "111", orderMain.getOrderId()));
        //Delete from DB
        service.delete(orderItem.getId());
    }

    @Test
    public void testSelect() {
        OrderItem orderItem = service.add(new OrderItem(5,555, "111", orderMain.getOrderId()));

        //Get from DB
        OrderItem orderItemFromDB = service.get(orderItem.getId());

        assertThat(orderItemFromDB.getTotalprice(), is(equalTo(555)));
        assertThat(categoryDao.get(productCardDao.get(orderItemFromDB.getProductCardSku()).getCategoryId()).getName(), is(equalTo("name")));
        assertThat(orderItemFromDB.getTotalprice(), isA(Integer.class));
        assertThat(orderItemFromDB, is(notNullValue()));
        assertThat(orderItemFromDB, is(anything()));
        assertThat(orderItemFromDB.getClass(), is(typeCompatibleWith(OrderItem.class)));
    }

    @Test
    public void testUpdate() {
        OrderItem orderItem = service.add(new OrderItem(5,555, "111", orderMain.getOrderId()));

        assertThat(orderItem.getAmount(), is(equalTo(5)));
        assertThat(orderItem.getTotalprice(), is(equalTo(555)));
        assertThat(orderItem.getAmount(), isA(Integer.class));
        assertThat(orderItem, is(notNullValue()));
        assertThat(orderItem, is(anything()));
        assertThat(orderItem.getTotalprice(), is(greaterThan(554)));
        assertThat(orderItem.getAmount(), is(not(lessThan(4))));
        assertThat(orderItem.getClass(), is(typeCompatibleWith(OrderItem.class)));

        orderItem.setAmount(6);

        //Update
        service.update(orderItem);

        //Get update info
        orderItem = service.add(orderItem);

        assertThat(orderItem.getAmount(), is(equalTo(6)));
    }

    @Test
    public void testGetAll(){
        OrderItem orderItem1 = service.add(new OrderItem(5,555, "111", orderMain.getOrderId()));
        OrderItem orderItem2 = service.add(new OrderItem(6,555, "111", orderMain.getOrderId()));
        OrderItem orderItem3 = service.add(new OrderItem(7,555, "111", orderMain.getOrderId()));


        //Record to DB
        service.add(orderItem1);
        service.add(orderItem2);
        service.add(orderItem3);


        List<OrderItem> orderItems = service.getAll();

        for(OrderItem o: orderItems) {
            assertThat(o.getAmount(), oneOf(5,6,7));
            assertThat(orderItems, is(notNullValue()));
            assertThat(orderItems, is(anything()));
            assertThat(orderItems.get(0), isA(OrderItem.class));
        }
    }


    @Test
    public void testGetByProductCard(){
        OrderItem orderItem1 = service.add(new OrderItem(5,555, "111", orderMain.getOrderId()));
        OrderItem orderItem2 = service.add(new OrderItem(6,555, "111", orderMain.getOrderId()));
        OrderItem orderItem3 = service.add(new OrderItem(7,555, "111", orderMain.getOrderId()));


        //Record to DB
        service.add(orderItem1);
        service.add(orderItem2);
        service.add(orderItem3);


        List<OrderItem> orderItems = service.getByProductCard("111");

        for(OrderItem o: orderItems) {
            assertThat(o.getAmount(), oneOf(5,6,7));
            assertThat(orderItems, is(notNullValue()));
            assertThat(orderItems, is(anything()));
            assertThat(orderItems.get(0), isA(OrderItem.class));
        }
    }

    @Test
    public void testGetByOrderMain(){
        OrderItem orderItem1 = service.add(new OrderItem(5,555, "111", orderMain.getOrderId()));
        OrderItem orderItem2 = service.add(new OrderItem(6,555, "111", orderMain.getOrderId()));
        OrderItem orderItem3 = service.add(new OrderItem(7,555, "111", orderMain.getOrderId()));


        //Record to DB
        service.add(orderItem1);
        service.add(orderItem2);
        service.add(orderItem3);


        List<OrderItem> orderItems = service.getByOrderMain(orderMain.getOrderId());

        for(OrderItem o: orderItems) {
            assertThat(o.getAmount(), oneOf(5,6,7));
            assertThat(orderItems, is(notNullValue()));
            assertThat(orderItems, is(anything()));
            assertThat(orderItems.get(0), isA(OrderItem.class));
        }
    }
}