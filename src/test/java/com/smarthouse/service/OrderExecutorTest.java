package com.smarthouse.service;

import com.smarthouse.dao.*;
import com.smarthouse.pojo.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class OrderExecutorTest {

    private OrderExecutor bl;
    private ProductCardDao productCardDao;
    private CategoryDao categoryDao;
    private CustomerDao customerDao;
    private OrderMainDao orderMainDao;
    private OrderItemDao orderItemDao;

    @Before
    public void init(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        productCardDao = (ProductCardDao) ac.getBean("productCardDao");
        categoryDao = (CategoryDao) ac.getBean("categoryDao");
        customerDao = (CustomerDao) ac.getBean("customerDao");
        orderMainDao = (OrderMainDao) ac.getBean("orderMainDao");
        orderItemDao = (OrderItemDao) ac.getBean("orderItemDao");
        bl = new OrderExecutor();
    }

    @After
    public void after(){
        orderItemDao.deleteAll();
        orderMainDao.deleteAll();
        customerDao.deleteAll();
        productCardDao.deleteAll();
        categoryDao.deleteAll();
    }

    @Test
    public void makeOrderTest(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));

        bl.makeOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "bell");

        assertThat(productCardDao.get("bell").getAmount(), is (equalTo(100)));

    }

    @Test(expected = NoSuchElementException.class)
    public void makeOrderTestExc(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));

        bl.makeOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 103, "bell");
    }

    @Test
    public void completeOrderTest(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));
        productCardDao.add(new ProductCard("ring", "ring signal", 1234, 50, 1, 1, "bell desc", category.getId()));
        productCardDao.add(new ProductCard("lord", "lord signal", 1234, 10, 1, 1, "bell desc", category.getId()));
        bl.makeOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "bell");
        bl.makeOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "ring");
        bl.makeOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "lord");

        List<OrderMain> orderMains = orderMainDao.getByCustomer("kya@bk.ru");
        for(OrderMain om: orderMains) {
            assertThat(om.getStatus() == 1, is(true));
            List<OrderItem> orderItems = orderItemDao.getByOrderMain(om.getOrderId());
            assertThat(productCardDao.get(orderItems.get(0).getProductCardSku()).getAmount(), oneOf(100, 50, 10));
        }

        bl.completeOrder("kya@bk.ru");

        orderMains = orderMainDao.getByCustomer("kya@bk.ru");

        for(OrderMain om: orderMains) {
            assertThat(om.getStatus() != 1, is(true));
            assertThat(om.getCustomerEmail(), is(equalTo("kya@bk.ru")));
            List<OrderItem> orderItems = orderItemDao.getByOrderMain(om.getOrderId());
            assertThat(productCardDao.get(orderItems.get(0).getProductCardSku()).getAmount(), oneOf(97, 47, 7));
        }

    }

    @Test(expected = NoSuchElementException.class)
    public void completeOrderTestExc(){
        bl.completeOrder("kkkkkkya@bk.ru");
    }

    @Test
    public void validateOrderTest(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));

        bl.makeOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "bell");

        assertThat(productCardDao.get("bell").getAmount(), is (equalTo(100)));

        assertThat(bl.validateOrder("kya@bk.ru"), is(true));
    }

    @Test
    public void getOrdersByCustomerTest(){
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
         customerDao.add(customer);

        OrderMain orderMain1 = new OrderMain("1OrderAddress", 1, customer.getEmail());
        OrderMain orderMain2 = new OrderMain("2OrderAddress", 1, customer.getEmail());
        OrderMain orderMain3 = new OrderMain("3OrderAddress", 1, customer.getEmail());

        //Record to DB
        orderMain1 = orderMainDao.add(orderMain1);
        orderMainDao.add(orderMain2);
        orderMainDao.add(orderMain3);

        List<OrderMain> ordersMain = orderMainDao.getByCustomer(customer.getEmail());

        for(OrderMain o: ordersMain) {
            assertThat(ordersMain, is(notNullValue()));
            assertThat(ordersMain, is(anything()));
            assertThat(orderMain1.getAddress(), oneOf("1OrderAddress", "2OrderAddress", "3OrderAddress"));
            assertThat(ordersMain.get(0), isA(OrderMain.class));
        }
    }

    @Test
    public void getItemOrdersByOrderMainTest(){
        Category category = categoryDao.add(new Category("desc", "name", 0));
        productCardDao.add(new ProductCard("111", "name", 123, 1, 1, 1, "decs", category.getId()));
        Customer customer = customerDao.add(new Customer("anniya@bk.ru", "Yuriy", false, "7585885"));
        OrderMain orderMain = orderMainDao.add(new OrderMain("OrderAddress", 1, customer.getEmail()));
        OrderItem orderItem1 = orderItemDao.add(new OrderItem(5,555, "111", orderMain.getOrderId()));
        OrderItem orderItem2 = orderItemDao.add(new OrderItem(6,555, "111", orderMain.getOrderId()));
        OrderItem orderItem3 = orderItemDao.add(new OrderItem(7,555, "111", orderMain.getOrderId()));


        //Record to DB
        orderItemDao.add(orderItem1);
        orderItemDao.add(orderItem2);
        orderItemDao.add(orderItem3);


        List<OrderItem> orderItems = orderItemDao.getByOrderMain(orderMain.getOrderId());

        for(OrderItem o: orderItems) {
            assertThat(o.getAmount(), oneOf(5,6,7));
            assertThat(orderItems, is(notNullValue()));
            assertThat(orderItems, is(anything()));
            assertThat(orderItems.get(0), isA(OrderItem.class));
        }
    }

    @Test
    public void getItemOrdersByProdCardTest(){
        Category category = categoryDao.add(new Category("desc", "name", 0));
        productCardDao.add(new ProductCard("111", "name", 123, 1, 1, 1, "decs", category.getId()));
        Customer customer = customerDao.add(new Customer("anniya@bk.ru", "Yuriy", false, "7585885"));
        OrderMain orderMain = orderMainDao.add(new OrderMain("OrderAddress", 1, customer.getEmail()));
        OrderItem orderItem1 = orderItemDao.add(new OrderItem(5,555, "111", orderMain.getOrderId()));
        OrderItem orderItem2 = orderItemDao.add(new OrderItem(6,555, "111", orderMain.getOrderId()));
        OrderItem orderItem3 = orderItemDao.add(new OrderItem(7,555, "111", orderMain.getOrderId()));

        //Record to DB
        orderItemDao.add(orderItem1);
        orderItemDao.add(orderItem2);
        orderItemDao.add(orderItem3);

        List<OrderItem> orderItems = orderItemDao.getByProductCard("111");

        for(OrderItem o: orderItems) {
            assertThat(o.getAmount(), oneOf(5,6,7));
            assertThat(orderItems, is(notNullValue()));
            assertThat(orderItems, is(anything()));
            assertThat(orderItems.get(0), isA(OrderItem.class));
        }
    }

}
