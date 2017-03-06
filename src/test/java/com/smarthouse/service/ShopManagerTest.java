package com.smarthouse.service;

import com.smarthouse.dao.*;
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
import java.util.*;
import static com.smarthouse.service.libs.enums.EnumProductSorter.*;
import static com.smarthouse.service.libs.enums.EnumSearcher.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/app-config.xml")
public class ShopManagerTest {

    @Resource
    private ShopManager shopManager;
    @Resource
    private ProductCardDao productCardDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private OrderMainDao orderMainDao;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private AttributeValueDao attributeValueDao;
    @Resource
    private AttributeNameDao attributeNameDao;
    @Resource
    private VisualizationDao visualizationDao;

    @BeforeClass
    public static void dropCreateDb() throws SQLException, InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        DbCreator dbCreator = (DbCreator) ac.getBean("dbCreator");
        dbCreator.dropCreateDbAndTables();
    }

    @After
    public void after(){
        orderItemDao.deleteAll();
        orderMainDao.deleteAll();
        customerDao.deleteAll();
        productCardDao.deleteAll();
        categoryDao.deleteAll();
        attributeValueDao.deleteAll();
        attributeNameDao.deleteAll();
        visualizationDao.deleteAll();
    }

    @Test
    public void isProductAvailable() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCard = productCardDao.add(productCard);
        assertTrue(shopManager.isProductAvailable(productCard.getSku()));
        productCardDao.delete(productCard.getSku());
        categoryDao.delete(category.getId());
    }

    @Test
    public void makeOrderTest(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));

        shopManager.createOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "bell");

        assertThat(productCardDao.get("bell").getAmount(), is (equalTo(100)));

    }

    @Test(expected = NoSuchElementException.class)
    public void makeOrderTestExc(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));

        shopManager.createOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 103, "bell");
    }

    @Test
    public void completeOrderTest(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));
        productCardDao.add(new ProductCard("ring", "ring signal", 1234, 50, 1, 1, "bell desc", category.getId()));
        productCardDao.add(new ProductCard("lord", "lord signal", 1234, 10, 1, 1, "bell desc", category.getId()));
        shopManager.createOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "bell");
        shopManager.createOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "ring");
        shopManager.createOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "lord");

        List<OrderMain> orderMains = orderMainDao.getByCustomer("kya@bk.ru");
        for(OrderMain om: orderMains) {
            assertThat(om.getStatus() == 1, is(true));
            List<OrderItem> orderItems = orderItemDao.getByOrderMain(om.getOrderId());
            assertThat(productCardDao.get(orderItems.get(0).getProductCardSku()).getAmount(), oneOf(100, 50, 10));
        }

        shopManager.submitOrder("kya@bk.ru");

        orderMains = orderMainDao.getByCustomer("kya@bk.ru");

        for(OrderMain om: orderMains) {
            assertThat(om.getStatus() != 1, is(true));
            assertThat(om.getCustomer().getEmail(), is(equalTo("kya@bk.ru")));
            List<OrderItem> orderItems = orderItemDao.getByOrderMain(om.getOrderId());
            assertThat(productCardDao.get(orderItems.get(0).getProductCardSku()).getAmount(), oneOf(97, 47, 7));
        }

    }

    @Test
    public void validateOrderTest(){
        Category category = categoryDao.add(new Category("desc", "catname", 0));
        productCardDao.add(new ProductCard("bell", "bell signal", 1234, 100, 1, 1, "bell desc", category.getId()));

        shopManager.createOrder("kya@bk.ru", "Yuriy", "0503337178", "my address", 3, "bell");

        assertThat(productCardDao.get("bell").getAmount(), is (equalTo(100)));

        assertThat(shopManager.validateOrder("kya@bk.ru"), is(true));
    }

    @Test
    public void getOrdersByCustomerTest(){
        Customer customer = new Customer("anniya@bk.ru", "Yuriy", false, "7585885");
         customerDao.add(customer);

        OrderMain orderMain1 = new OrderMain("1OrderAddress", 1, customer);
        OrderMain orderMain2 = new OrderMain("2OrderAddress", 1, customer);
        OrderMain orderMain3 = new OrderMain("3OrderAddress", 1, customer);

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
        OrderMain orderMain = orderMainDao.add(new OrderMain("OrderAddress", 1, customer));
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
        OrderMain orderMain = orderMainDao.add(new OrderMain("OrderAddress", 1, customer));
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

    @Test
    public void checkCorrectionOfFindAllProductsByCriteria() throws Exception {

        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "name111", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "name111", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCardDao.add(productCard1);
        productCardDao.add(productCard2);

        Set<ProductCard> allProducts = shopManager.findAllProducts("name111");
        assertTrue(allProducts.size() == 2);

        productCardDao.delete("888");
        productCardDao.delete("999");
        categoryDao.delete(category.getId());
    }

    @Test
    public void checkCorrectionOfFindAllProductsByCriteriaAndPlaceForFind() throws Exception {

        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "name111", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "name111", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCardDao.add(productCard1);
        productCardDao.add(productCard2);

        Set<ProductCard> allProducts = shopManager.findProducts("name111", FIND_BY_NAME);
        assertTrue(allProducts.size() == 2);
        allProducts = shopManager.findProducts("xxx", FIND_IN_PROD_DESC);
        assertTrue(allProducts.size() == 2);
        allProducts = shopManager.findProducts("888", FIND_BY_SKU);
        assertTrue(allProducts.size() == 1);

        productCardDao.delete("888");
        productCardDao.delete("999");
        categoryDao.delete(category.getId());
    }

    @Test
    public void getRootCategory() throws Exception {

        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        List<Category> list = shopManager.getRootCategory();
        assertThat(list.get(0).getName(), is(equalTo("name")));

        categoryDao.delete(category.getId());
    }

    @Test
    public void getSubCategories() throws Exception {

        Category category1 = new Category("desc", "name", 0);
        category1 = categoryDao.add(category1);
        Category category2 = new Category("desc", "name", category1.getId());
        category2 = categoryDao.add(category2);

        List<Category> list = shopManager.getSubCategories(categoryDao.get(category1.getId()));
        assertThat(list.get(0).getName(), oneOf("catname1", "catname31", "name"));
        assertThat(list.get(0).getName(), is(notNullValue()));
        assertThat(list.get(0).getName(), isA(String.class));
        assertThat(list.get(0).getCategoryId(), isA(Integer.class));

        categoryDao.delete(category2.getId());
        categoryDao.delete(category1.getId());
    }

    @Test
    public void getProductCardsByCategory() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "3name", 3333, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard3 = new ProductCard("000", "4name", 444, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCard1 = productCardDao.add(productCard1);
        productCard2 = productCardDao.add(productCard2);
        productCard3 = productCardDao.add(productCard3);

        List<ProductCard> productCards = shopManager.getProductCardsByCategory(category);

        assertThat(productCard1.getName(), is (equalTo("2name")));
        assertThat(productCard2.getName(), is (equalTo("3name")));
        assertThat(productCard3.getName(), is (equalTo("4name")));
        assertThat(productCards, is(notNullValue()));
        assertThat(productCards, is(anything()));
        assertThat(productCards.get(1), isA(ProductCard.class));

        productCardDao.delete("888");
        productCardDao.delete("999");
        productCardDao.delete("000");

        categoryDao.delete(category.getId());

    }

    @Test
    public void getVisualListByProduct() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        //Record to DB
        Visualization visualization1 = new Visualization(2, "1url", productCard.getSku());
        Visualization visualization2 = new Visualization(4, "2url", productCard.getSku());
        Visualization visualization3 = new Visualization(6, "3url", productCard.getSku());

        visualization1 = visualizationDao.add(visualization1);
        visualization2 = visualizationDao.add(visualization2);
        visualization3 = visualizationDao.add(visualization3);

        List<Visualization> visualizations = shopManager.getVisualListByProduct(productCard);

        assertThat(visualizations, is(notNullValue()));
        assertThat(visualizations, is(anything()));
        assertThat(visualizations.get(0), isA(Visualization.class));
        assertThat(visualization1.getUrl(), is(equalTo("1url")));
        assertThat(visualization2.getType(), is(equalTo(4)));
        assertThat(visualization3.getUrl(), is(equalTo("3url")));

        //Delete from DB
        visualizationDao.deleteAll();
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
    }

    @Test
    public void getAttrValuesByProduct() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        AttributeName attributeName = new AttributeName("color");
        attributeNameDao.add(attributeName);
        AttributeValue attributeValue = new AttributeValue("1", "color", "222");
        attributeValueDao.add(attributeValue);
        attributeValue = new AttributeValue("2", "color", "222");
        attributeValueDao.add(attributeValue);
        attributeValue = new AttributeValue("3", "color", "222");
        attributeValueDao.add(attributeValue);

        List<AttributeValue> list = shopManager.getAttrValuesByProduct(productCardDao.get("222"));

        list.forEach(a -> {
            assertThat(a.getValue(), oneOf("1", "2", "3"));
            assertThat(a, is(notNullValue()));
            assertThat(a.getValue(), isA(String.class));
            assertThat(a, isA(AttributeValue.class));
        });
        attributeValueDao.deleteAll();
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
        attributeNameDao.delete("color");
    }

    @Test
    public void getAttrValuesByName() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = categoryDao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = productCardDao.add(productCard);
        AttributeName attributeName = new AttributeName("color");
        attributeNameDao.add(attributeName);
        AttributeValue attributeValue = new AttributeValue("1", "color", "222");
        attributeValueDao.add(attributeValue);
        attributeValue = new AttributeValue("2", "color", "222");
        attributeValueDao.add(attributeValue);
        attributeValue = new AttributeValue("3", "color", "222");
        attributeValueDao.add(attributeValue);

        List<AttributeValue> list = shopManager.getAttrValuesByName(attributeNameDao.get("color"));

        list.forEach(a -> {
            assertThat(a.getValue(), oneOf("1", "2", "3"));
            assertThat(a, is(notNullValue()));
            assertThat(a.getValue(), isA(String.class));
            assertThat(a, isA(AttributeValue.class));
        });
        attributeValueDao.deleteAll();
        productCardDao.delete("222");
        categoryDao.delete(category.getId());
        attributeNameDao.delete("color");
    }


    @Test
    public void sortByPopularity() throws Exception {
        List<ProductCard> productCards = productCardDao.getAll();
        productCards.forEach(a -> System.out.print(a.getLikes() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = shopManager.productSort(productCards, SORT_BY_POPULARITY);
        productCardsSorted.forEach(a -> System.out.print(a.getLikes() + "  "));
    }

    @Test
    public void sortByUnpopularity() throws Exception {
        List<ProductCard> productCards = productCardDao.getAll();
        productCards.forEach(a -> System.out.print(a.getDislikes() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = shopManager.productSort(productCards, SORT_BY_UNPOPULARITY);
        productCardsSorted.forEach(a -> System.out.print(a.getDislikes() + "  "));
    }

    @Test
    public void sortByName() throws Exception {
        List<ProductCard> productCards = productCardDao.getAll();
        productCards.forEach(a -> System.out.print(a.getName() + " "));
        List<ProductCard> productCardsSorted = shopManager.productSort(productCards, SORT_BY_NAME);
        System.out.println(productCards.size());
        System.out.println(productCardsSorted.size());
        productCardsSorted.forEach(a -> System.out.print(a.getName() + "  "));
    }

    @Test
    public void sortByNameReversed() throws Exception {
        List<ProductCard> productCards = productCardDao.getAll();
        productCards.forEach(a -> System.out.print(a.getName() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = shopManager.productSort(productCards, SORT_BY_NAME_REVERSED);
        productCardsSorted.forEach(a -> System.out.print(a.getName() + "  "));
    }

    @Test
    public void sortByLowerPrice() throws Exception {
        List<ProductCard> productCards = productCardDao.getAll();
        productCards.forEach(a -> System.out.print(a.getPrice() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = shopManager.productSort(productCards, SORT_BY_LOW_PRICE);
        productCardsSorted.forEach(a -> System.out.print(a.getPrice() + "  "));
    }

    @Test
    public void sortByHigherPrice() throws Exception {
        List<ProductCard> productCards = productCardDao.getAll();
        productCards.forEach(a -> System.out.print(a.getPrice() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = shopManager.productSort(productCards, SORT_BY_HIGH_PRICE);
        productCardsSorted.forEach(a -> System.out.print(a.getPrice() + "  "));
    }

    @Test
    public void sortByAmount() throws Exception {
        List<ProductCard> productCards = productCardDao.getAll();
        productCards.forEach(a -> System.out.print(a.getAmount() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = shopManager.productSort(productCards, SORT_BY_AMOUNT);
        productCardsSorted.forEach(a -> System.out.print(a.getAmount() + "  "));
    }

}
