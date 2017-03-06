package com.smarthouse.dao;

import com.smarthouse.pojo.Customer;
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
public class CustomerDaoTest {

    @Resource
    private CustomerDao service;

    @BeforeClass
    public static void dropCreateDb() throws SQLException, InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        DbCreator dbCreator = (DbCreator) ac.getBean("dbCreator");
        dbCreator.dropCreateDbAndTables();
    }

    @Test
    public void testSaveRecord(){
        Customer customer1 = new Customer("anniya@bk.ru", "4customerName", false,  "0503337178");

        //Record to DB
        Customer customer = service.add(customer1);

        assertThat(customer.getEmail(), is(equalTo("anniya@bk.ru")));
        assertThat(customer.getName(), is(equalTo("4customerName")));
        assertThat(customer.isSubscribe(), is(not(true)));
        assertThat(customer.getPhone(), is(equalTo("0503337178")));
        assertThat(customer.getName(), isA(String.class));
        assertThat(customer, is(notNullValue()));
        assertThat(customer, is(anything()));
        assertThat(customer.getName(), is(greaterThan("")));
        assertThat(customer.getName(), is(not(lessThan(""))));
        assertThat(customer.getClass(), is(typeCompatibleWith(Customer.class)));

        service.delete("anniya@bk.ru");
    }

    @Test
    public void testDeleteRecord(){
        Customer customer1 = new Customer("anniya@bk.ru", "2customerName", false,  "087696966");

        //Record to DB
        Customer customer = service.add(customer1);


        //Delete from DB
        service.delete(customer.getEmail());
    }

    @Test
    public void testSelect(){
        Customer customer1 = new Customer("anniya@bk.ru", "2customerName", false,  "087696966");

        //Record to DB
        Customer customer = service.add(customer1);

        //Get from DB
        Customer customerFromDb = service.get(customer.getEmail());

        assertThat(customerFromDb.getEmail(), is(equalTo("anniya@bk.ru")));
        assertThat(customerFromDb.getName(), is(equalTo("2customerName")));
        assertThat(customerFromDb.isSubscribe(), is(not(true)));
        assertThat(customerFromDb.getPhone(), is(equalTo("087696966")));
        assertThat(customerFromDb.getName(), isA(String.class));
        assertThat(customerFromDb, is(notNullValue()));
        assertThat(customerFromDb, is(anything()));
        assertThat(customerFromDb.getName(), is(greaterThan("")));
        assertThat(customerFromDb.getName(), is(not(lessThan(""))));
        assertThat(customerFromDb.getClass(), is(typeCompatibleWith(Customer.class)));

        //Delete from DB
        service.delete(customerFromDb.getEmail());
    }

    @Test
    public void testUpdate(){
        Customer customer1 = new Customer("anniya@bk.ru", "2customerName", false,  "087696966");

        //Record to DB
        Customer customer = service.add(customer1);

        assertThat(customer.getEmail(), is(equalTo("anniya@bk.ru")));
        assertThat(customer.getName(), is(equalTo("2customerName")));
        assertThat(customer.isSubscribe(), is(not(true)));
        assertThat(customer.getPhone(), is(equalTo("087696966")));
        assertThat(customer.getName(), isA(String.class));
        assertThat(customer, is(notNullValue()));
        assertThat(customer, is(anything()));
        assertThat(customer.getName(), is(greaterThan("")));
        assertThat(customer.getName(), is(not(lessThan(""))));
        assertThat(customer.getClass(), is(typeCompatibleWith(Customer.class)));

        customer1.setName("4cust");

        //Update
        service.update(customer1);

        //Get update info
        Customer customer2 = service.get(customer1.getEmail());

        assertThat(customer2.getName(), is(equalTo("4cust")));
        assertThat(customer2.getName(), isA(String.class));
        assertThat(customer2, is(notNullValue()));
        assertThat(customer2, is(anything()));
        assertThat(customer2.getName(), is(greaterThan("")));
        assertThat(customer2.getName(), is(not(lessThan(""))));
        assertThat(customer2.getClass(), is(typeCompatibleWith(Customer.class)));

        //Delete from DB
        service.delete(customer2.getEmail());
    }

    @Test
    public void testGetAll(){
        Customer customer1 = new Customer("anniya@bk.ru", "2customerName", false,  "087696966");
        Customer customer2 = new Customer("2rrre@bk.ru", "3customerName", false,  "678889868");
        Customer customer3 = new Customer("2vbn@bk.ru", "22customerName", false,  "698698");

        customer1 = service.add(customer1);
        customer2 = service.add(customer2);
        customer3 = service.add(customer3);

        List<Customer> attributeNames = service.getAll();

        assertThat(customer1.getName(), is(equalTo("2customerName")));
        assertThat(customer2.getName().equals("3customerName"), is(true));
        assertThat(customer3.getName().equals("22customerName"), is(true));
        assertThat(attributeNames, is(notNullValue()));
        assertThat(attributeNames, is(anything()));
        assertThat(attributeNames.get(0), isA(Customer.class));

        //Delete from DB
        service.delete("anniya@bk.ru");
        service.delete("2rrre@bk.ru");
        service.delete("2vbn@bk.ru");
    }

    @Test
    public void testIsCustomerExist(){
        Customer customer1 = new Customer("anniya@bk.ru", "2customerName", false,  "087696966");
        service.add(customer1);

        assertThat(service.isEmailExist("anniya@bk.ru"), is (true));

        service.delete("anniya@bk.ru");
    }
}
