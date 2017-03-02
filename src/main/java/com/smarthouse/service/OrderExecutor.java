package com.smarthouse.service;

import com.smarthouse.dao.*;
import com.smarthouse.pojo.*;
import com.smarthouse.pojo.validators.EmailValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.smarthouse.service.libs.util.Checker.isCustomerExist;
import static com.smarthouse.service.libs.util.Checker.isProductAvailable;
import static com.smarthouse.service.libs.util.Checker.isRequiredAmountOfProductCardAvailable;

public class OrderExecutor {

    private ProductCardDao productCardDao;
    private CustomerDao customerDao;
    private OrderMainDao orderMainDao;
    private OrderItemDao orderItemDao;

    public OrderExecutor() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        productCardDao = (ProductCardDao) ac.getBean("productCardDao");
        customerDao = (CustomerDao) ac.getBean("customerDao");
        orderMainDao = (OrderMainDao) ac.getBean("orderMainDao");
        orderItemDao = (OrderItemDao) ac.getBean("orderItemDao");
    }

    /**
     * Method makeOrder is add or update new Customer into database
     * and also add order info in DB with compute total price
     *
     * @param email   user email address for identy each user by primary key
     * @param name    name of user (optional)
     * @param phone   phone number of user (optional)
     * @param address address for receive order
     * @param amount  amount of products in order
     * @param sku     unique id of each product
     */
    public void makeOrder(String email, String name, String phone,
                          String address, int amount, String sku) {

        EmailValidator emailValidator = new EmailValidator();

        if (!emailValidator.validate(email))
            throw new ValidationException("Email not valid");

        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        customerDao = (CustomerDao) ac.getBean("customerDao");
        orderMainDao = (OrderMainDao) ac.getBean("orderMainDao");
        orderItemDao = (OrderItemDao) ac.getBean("orderItemDao");

        ProductCard productCard = productCardDao.get(sku);

        if (!isRequiredAmountOfProductCardAvailable(sku, amount))
            throw new NoSuchElementException();

        int totalPrice = productCard.getPrice() * amount;

        Customer customer = new Customer(email, name, true, phone);

        if(isCustomerExist(email))
            customerDao.update(customer);
        else
            customerDao.add(customer);

        OrderMain orderMain = orderMainDao.add(new OrderMain(address, 1, customer.getEmail()));
        OrderItem orderItem = new OrderItem(amount, totalPrice, productCard.getSku(), orderMain.getOrderId());
        orderItemDao.add(orderItem);
    }

    /**
     * Method completeOrder need for update amount of ProductCard
     * on warehouse and update status of order in OrderMain in tables
     *
     * @param email is  a user email for making changes
     * @return void type
     * @throws NoSuchElementException if amount of products in our order
     *                                less than on warehouse
     */
    public void completeOrder(String email) {
        if (validateOrder(email) && isCustomerExist(email)) {
            Customer customer = customerDao.get(email);
            List<OrderMain> ordersByCustomer = getOrdersByCustomer(customer);

            for (OrderMain om : ordersByCustomer) {

                if (om.getStatus() != 1)
                    continue;

                List<OrderItem> orderItemsByOrderMain = getItemOrdersByOrderMain(om);
                for (OrderItem oi : orderItemsByOrderMain) {
                    ProductCard productCard = productCardDao.get(oi.getProductCardSku());
                    int newAmount = productCard.getAmount() - oi.getAmount();
                    productCard.setAmount(newAmount);
                    productCardDao.update(productCard);
                }

                om.setStatus(2);
                orderMainDao.update(om);
            }
        } else
            throw new NoSuchElementException("This amount of products not exist on our warehouse");
    }

    /**
     * Method validateOrder need for check amount of ProductCard
     * on warehouse.
     *
     * @param email is  a user email for making changes
     * @return boolean type. True if amount in order >= amount on
     * warehouse
     */
    public boolean validateOrder(String email) {
        boolean isExist = true;
        Customer customer = customerDao.get(email);
        List<OrderMain> ordersByCustomer = getOrdersByCustomer(customer);
        l1:
        for (OrderMain om : ordersByCustomer) {
            List<OrderItem> itemOrdersByOrderMain = getItemOrdersByOrderMain(om);
            for (OrderItem oi : itemOrdersByOrderMain) {
                if (!isProductAvailable(oi.getProductCardSku())) {
                    isExist = false;
                    break l1;
                }
            }
        }
        return isExist;
    }

    public List<OrderMain> getOrdersByCustomer(Customer customer) {
        return orderMainDao.getByCustomer(customer.getEmail());
    }

    public List<OrderItem> getItemOrdersByOrderMain(OrderMain orderMain) {
        return orderItemDao.getByOrderMain(orderMain.getOrderId());
    }

    public List<OrderItem> getItemOrdersByProdCard(ProductCard productCard) {
        return orderItemDao.getByProductCard(productCard.getSku());
    }


}
