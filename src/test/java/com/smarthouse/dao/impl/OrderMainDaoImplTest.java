package com.smarthouse.dao.impl;

import com.smarthouse.dao.CustomerDao;
import com.smarthouse.dao.OrderMainDao;
import com.smarthouse.pojo.Customer;
import com.smarthouse.pojo.OrderMain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/app-config.xml")
public class OrderMainDaoImplTest {

    @Resource
    private OrderMainDao service;
    @Resource
    private CustomerDao cdao;

    @Test
    public void testSaveRecord() {
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
        cdao.add(customer);
        OrderMain orderMain = new OrderMain("OrderAddress", 1, customer.getEmail());

        //Record to DB
        orderMain = service.add(orderMain);

        assertThat(orderMain.getAddress(), is(equalTo("OrderAddress")));
        assertThat(orderMain.getCustomerEmail(), is(equalTo("anniya@bk.ru")));
        assertThat(orderMain.getAddress(), isA(String.class));
        assertThat(orderMain, is(notNullValue()));
        assertThat(orderMain, is(anything()));
        assertThat(orderMain.getAddress(), is(greaterThan("")));
        assertThat(orderMain.getAddress(), is(not(lessThan(""))));
        assertThat(orderMain.getClass(), is(typeCompatibleWith(OrderMain.class)));

        service.delete(orderMain.getOrderId());
        cdao.delete(orderMain.getCustomerEmail());
    }

    @Test
    public void testDeleteRecord() {
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
        cdao.add(customer);
        OrderMain orderMain = new OrderMain("OrderAddress", 1, customer.getEmail());

        //Record to DB
        orderMain = service.add(orderMain);

        //Delete from DB
        service.delete(orderMain.getOrderId());
        cdao.delete(orderMain.getCustomerEmail());
    }

    @Test
    public void testSelect() {
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
        cdao.add(customer);
        OrderMain orderMain = new OrderMain("OrderAddress", 1, customer.getEmail());

        //Record to DB
        orderMain = service.add(orderMain);

        //Get from DB
        OrderMain orderMainFromDB = service.get(orderMain.getOrderId());

        assertThat(orderMainFromDB.getAddress(), is(equalTo("OrderAddress")));
        assertThat(orderMainFromDB.getCustomerEmail(), is(equalTo("anniya@bk.ru")));
        assertThat(orderMainFromDB.getAddress(), isA(String.class));
        assertThat(orderMainFromDB, is(notNullValue()));
        assertThat(orderMainFromDB, is(anything()));
        assertThat(orderMainFromDB.getAddress(), is(greaterThan("")));
        assertThat(orderMainFromDB.getAddress(), is(not(lessThan(""))));
        assertThat(orderMainFromDB.getClass(), is(typeCompatibleWith(OrderMain.class)));

        //Delete from DB
        service.delete(orderMain.getOrderId());
        cdao.delete(orderMain.getCustomerEmail());
    }

    @Test
    public void testUpdate() {
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
        cdao.add(customer);
        OrderMain orderMain = new OrderMain("OrderAddress", 1, customer.getEmail());

        //Record to DB
        orderMain = service.add(orderMain);

        assertThat(orderMain.getAddress(), is(equalTo("OrderAddress")));
        assertThat(orderMain.getCustomerEmail(), is(equalTo("anniya@bk.ru")));
        assertThat(orderMain.getAddress(), isA(String.class));
        assertThat(orderMain, is(notNullValue()));
        assertThat(orderMain, is(anything()));
        assertThat(orderMain.getAddress(), is(greaterThan("")));
        assertThat(orderMain.getAddress(), is(not(lessThan(""))));
        assertThat(orderMain.getClass(), is(typeCompatibleWith(OrderMain.class)));

        orderMain.setAddress("newAddress");

        //Update
        service.update(orderMain);

        assertThat(orderMain.getAddress(), is(equalTo("newAddress")));
        assertThat(orderMain.getCustomerEmail(), is(equalTo("anniya@bk.ru")));
        assertThat(orderMain.getAddress(), isA(String.class));
        assertThat(orderMain, is(notNullValue()));
        assertThat(orderMain, is(anything()));
        assertThat(orderMain.getAddress(), is(greaterThan("")));
        assertThat(orderMain.getAddress(), is(not(lessThan(""))));
        assertThat(orderMain.getClass(), is(typeCompatibleWith(OrderMain.class)));

        //Delete from DB
        service.delete(orderMain.getOrderId());
        cdao.delete(orderMain.getCustomerEmail());
    }

    @Test
    public void testGetAll(){
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
        cdao.add(customer);

        OrderMain orderMain1 = new OrderMain("1OrderAddress", 1, customer.getEmail());
        OrderMain orderMain2 = new OrderMain("2OrderAddress", 1, customer.getEmail());
        OrderMain orderMain3 = new OrderMain("3OrderAddress", 1, customer.getEmail());

        //Record to DB
        orderMain1 = service.add(orderMain1);
        orderMain2 = service.add(orderMain2);
        orderMain3 = service.add(orderMain3);

        List<OrderMain> ordersMain = service.getAll();

        assertThat(orderMain1.getAddress(), is(equalTo("1OrderAddress")));
        assertThat(orderMain2.getAddress(), is(equalTo("2OrderAddress")));
        assertThat(orderMain3.getAddress(), is(equalTo("3OrderAddress")));
        assertThat(ordersMain, is(notNullValue()));
        assertThat(ordersMain, is(anything()));
        assertThat(ordersMain.get(0), isA(OrderMain.class));

        //Delete from DB
        service.delete(orderMain1.getOrderId());
        service.delete(orderMain2.getOrderId());
        service.delete(orderMain3.getOrderId());
        cdao.delete(orderMain1.getCustomerEmail());
    }

    @Test
    public void testGetByCustomer() {
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
        cdao.add(customer);

        OrderMain orderMain1 = new OrderMain("1OrderAddress", 1, customer.getEmail());
        OrderMain orderMain2 = new OrderMain("2OrderAddress", 1, customer.getEmail());
        OrderMain orderMain3 = new OrderMain("3OrderAddress", 1, customer.getEmail());

        //Record to DB
        orderMain1 = service.add(orderMain1);
        orderMain2 = service.add(orderMain2);
        orderMain3 = service.add(orderMain3);

        List<OrderMain> ordersMain = service.getByCustomer(customer.getEmail());

        for(OrderMain o: ordersMain) {
            assertThat(orderMain1.getAddress(), oneOf("1OrderAddress", "2OrderAddress", "3OrderAddress"));
            assertThat(ordersMain, is(notNullValue()));
            assertThat(ordersMain, is(anything()));
            assertThat(ordersMain.get(0), isA(OrderMain.class));
        }

        //Delete from DB
        service.delete(orderMain1.getOrderId());
        service.delete(orderMain2.getOrderId());
        service.delete(orderMain3.getOrderId());
        cdao.delete(orderMain1.getCustomerEmail());
    }


}