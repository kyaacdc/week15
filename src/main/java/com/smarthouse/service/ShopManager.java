package com.smarthouse.service;

import com.smarthouse.dao.*;
import com.smarthouse.pojo.*;
import com.smarthouse.pojo.validators.EmailValidator;
import com.smarthouse.service.libs.enums.EnumProductSorter;
import com.smarthouse.service.libs.enums.EnumSearcher;

import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static com.smarthouse.service.libs.enums.EnumSearcher.FIND_ALL;

public class ShopManager {

    private ProductCardDao productCardDao;
    private CategoryDao categoryDao;
    private CustomerDao customerDao;
    private OrderMainDao orderMainDao;
    private OrderItemDao orderItemDao;
    private VisualizationDao visualizationDao;
    private AttributeValueDao attributeValueDao;


    public ShopManager() {}

    public ShopManager(ProductCardDao productCardDao, CategoryDao categoryDao,
                       CustomerDao customerDao, OrderMainDao orderMainDao,
                       OrderItemDao orderItemDao, VisualizationDao visualizationDao,
                       AttributeValueDao attributeValueDao)
    {
        this.productCardDao = productCardDao;
        this.categoryDao = categoryDao;
        this.customerDao = customerDao;
        this.orderMainDao = orderMainDao;
        this.orderItemDao = orderItemDao;
        this.visualizationDao = visualizationDao;
        this.attributeValueDao = attributeValueDao;
    }

    /**
     * Method createOrder is add or update new Customer into database
     * and also add order info in DB with compute total price
     *
     * @param email   user email address for identy each user by primary key
     * @param name    name of user (optional)
     * @param phone   phone number of user (optional)
     * @param address address for receive order
     * @param amount  amount of products in order
     * @param sku     unique id of each product
     */
    public void createOrder(String email, String name, String phone,
                            String address, int amount, String sku) {

        EmailValidator emailValidator = new EmailValidator();

        if (!emailValidator.validate(email))
            throw new ValidationException("Email not valid");

        ProductCard productCard = productCardDao.get(sku);

        if (!isRequiredAmountOfProductCardAvailable(sku, amount))
            throw new NoSuchElementException();

        int totalPrice = productCard.getPrice() * amount;

        Customer customer = new Customer(email, name, true, phone);

        if(isCustomerExist(email))
            customerDao.update(customer);
        else
            customerDao.add(customer);

        OrderMain orderMain = orderMainDao.add(new OrderMain(address, 1, customer));
        OrderItem orderItem = new OrderItem(amount, totalPrice, productCard.getSku(), orderMain.getOrderId());
        orderItemDao.add(orderItem);
    }

    /**
     * Method submitOrder need for update amount of ProductCard
     * on warehouse and update status of order in OrderMain in tables
     *
     * @param email is  a user email for making changes
     * @return void type
     * @throws NoSuchElementException if amount of products in our order
     *                                less than on warehouse
     */
    public void submitOrder(String email) {
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

    //Return product availabitity in storehouse by amount
    public boolean isRequiredAmountOfProductCardAvailable(String sku, int amount){

        ProductCard productCard = productCardDao.get(sku);

        int productCardAmount = productCard.getAmount();

        return amount <= productCardAmount;
    }

    //Return product availabitity in storehouse
    public boolean isProductAvailable(String sku) {
        return productCardDao.get(sku).getAmount() > 0;
    }

    //Return is customer exist
    public boolean isCustomerExist(String email) {
        return customerDao.isEmailExist(email);
    }



    /**
     *   Method findProduct need for find any ProductCard
     *   on warehouse by String criteria in all Db     *
     *  @param criteria  String is  a string for the find
     *  @return Set<ProductCard> type with found set of products
     */
    public Set<ProductCard> findAllProducts(String criteria){
        return FIND_ALL.findAll(criteria);
    }

    /**
     *   Method findProduct need for find any ProductCard
     *   on warehouse by String criteria, in custom place.
     *  @param criteria  String is  a string for the find
     *  @param placeForFind enumeration for choose sort criteria:
     *                      FIND_ALL,
     *                      FIND_BY_NAME,
     *                      FIND_IN_PROD_DESC,
     *                      FIND_IN_CATEGORY_NAME,
     *                      FIND_IN_CATEGORY_DESC;
     *  @return Set<ProductCard> found results of products
     */
    public Set<ProductCard> findProducts(String criteria, EnumSearcher placeForFind){
        Set<ProductCard> result;
        List<ProductCard> all = productCardDao.getAll();
        result = placeForFind.findByPlace(criteria, all, placeForFind);
        return result;
    }

// Methods for getting lists of various items

    public List<Category> getRootCategory(){
        return categoryDao.getAll().stream()
                .filter(a -> a.getCategoryId() == 0)
                .collect(Collectors.toList());
    }

    public List<Category> getSubCategories(Category category){
        return categoryDao.getSubCategories(category);
    }

    public List<ProductCard> getProductCardsByCategory(Category category){
        return productCardDao.getByCategory(category.getId());
    }

    public List<Visualization> getVisualListByProduct(ProductCard productCard){
        return visualizationDao.getByProductCard(productCard.getSku());
    }

    public List<AttributeValue> getAttrValuesByProduct(ProductCard productCard){
        return attributeValueDao.getByProductCard(productCard.getSku());
    }

    public List<AttributeValue> getAttrValuesByName(AttributeName attributeName){
        return attributeValueDao.getByAttributeName(attributeName.getName());
    }

    public List<ProductCard> productSort(List<ProductCard> list, EnumProductSorter criteria){
        return list.stream()
                .sorted(criteria)
                .collect(Collectors.toList());
    }

}
