package com.smarthouse.service;

import com.smarthouse.dao.*;
import com.smarthouse.pojo.*;
import com.smarthouse.service.libs.util.Checker;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.smarthouse.service.libs.enums.EnumSearcher.FIND_BY_NAME;
import static com.smarthouse.service.libs.enums.EnumSearcher.FIND_BY_SKU;
import static com.smarthouse.service.libs.enums.EnumSearcher.FIND_IN_PROD_DESC;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ProductCardGetterTest {

    private ProductCardGetter bl;
    private CategoryDao cdao;
    private ProductCardDao pdao;
    private AttributeValueDao avdao;
    private AttributeNameDao andao;
    private VisualizationDao vdao;

    @Before
    public void init(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        cdao = (CategoryDao) ac.getBean("categoryDao");
        pdao = (ProductCardDao) ac.getBean("productCardDao");
        avdao = (AttributeValueDao) ac.getBean("attributeValueDao");
        andao = (AttributeNameDao) ac.getBean("attributeNameDao");
        vdao = (VisualizationDao) ac.getBean("visualizationDao");
        bl = new ProductCardGetter();
    }

    @Test
    public void isProductAvailable() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);

        ProductCard productCard = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCard = pdao.add(productCard);
        assertTrue(Checker.isProductAvailable(productCard.getSku()));
        pdao.delete(productCard.getSku());
        cdao.delete(category.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void isProductAvailableThrownIfNotBelong(){
        Checker.isProductAvailable("2222");
    }

    @Test
    public void checkCorrectionOfFindAllProductsByCriteria() throws Exception {

        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);

        ProductCard productCard1 = new ProductCard("888", "name111", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "name111", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        pdao.add(productCard1);
        pdao.add(productCard2);

        Set<ProductCard> allProducts = bl.findAllProducts("name111");
        assertTrue(allProducts.size() == 2);

        pdao.delete("888");
        pdao.delete("999");
        cdao.delete(category.getId());
    }

    @Test
    public void checkCorrectionOfFindAllProductsByCriteriaAndPlaceForFind() throws Exception {

        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);

        ProductCard productCard1 = new ProductCard("888", "name111", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "name111", 2222, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        pdao.add(productCard1);
        pdao.add(productCard2);

        Set<ProductCard> allProducts = bl.findProducts("name111", FIND_BY_NAME);
        assertTrue(allProducts.size() == 2);
        allProducts = bl.findProducts("xxx", FIND_IN_PROD_DESC);
        assertTrue(allProducts.size() == 2);
        allProducts = bl.findProducts("888", FIND_BY_SKU);
        assertTrue(allProducts.size() == 1);

        pdao.delete("888");
        pdao.delete("999");
        cdao.delete(category.getId());
    }

    @Test
    public void getRootCategory() throws Exception {

        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);

        List<Category> list = bl.getRootCategory();
        assertThat(list.get(0).getName(), is(equalTo("name")));

        cdao.delete(category.getId());
    }

    @Test
    public void getSubCategories() throws Exception {

        Category category1 = new Category("desc", "name", 0);
        category1 = cdao.add(category1);
        Category category2 = new Category("desc", "name", category1.getId());
        category2 = cdao.add(category2);

        List<Category> list = bl.getSubCategories(cdao.get(category1.getId()));
        assertThat(list.get(0).getName(), oneOf("catname1", "catname31", "name"));
        assertThat(list.get(0).getName(), is(notNullValue()));
        assertThat(list.get(0).getName(), isA(String.class));
        assertThat(list.get(0).getCategoryId(), isA(Integer.class));

        cdao.delete(category2.getId());
        cdao.delete(category1.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void getSubCategoriesExc() {
       bl.getSubCategories(cdao.get(11111111));
    }

    @Test
    public void getProductCardsByCategory() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);

        ProductCard productCard1 = new ProductCard("888", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard2 = new ProductCard("999", "3name", 3333, 34, 45, 4, "xxx", category.getId());
        ProductCard productCard3 = new ProductCard("000", "4name", 444, 34, 45, 4, "xxx", category.getId());

        //Record to DB
        productCard1 = pdao.add(productCard1);
        productCard2 = pdao.add(productCard2);
        productCard3 = pdao.add(productCard3);

        List<ProductCard> productCards = bl.getProductCardsByCategory(category);

        assertThat(productCard1.getName(), is (equalTo("2name")));
        assertThat(productCard2.getName(), is (equalTo("3name")));
        assertThat(productCard3.getName(), is (equalTo("4name")));
        assertThat(productCards, is(notNullValue()));
        assertThat(productCards, is(anything()));
        assertThat(productCards.get(1), isA(ProductCard.class));

        pdao.delete("888");
        pdao.delete("999");
        pdao.delete("000");

        cdao.delete(category.getId());

    }

    @Test(expected = NoSuchElementException.class)
    public void getProductCardsByCategoryExc() {
        bl.getProductCardsByCategory(cdao.get(100100000));
    }

    @Test
    public void getVisualListByProduct() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = pdao.add(productCard);
        //Record to DB
        Visualization visualization1 = new Visualization(2, "1url", productCard.getSku());
        Visualization visualization2 = new Visualization(4, "2url", productCard.getSku());
        Visualization visualization3 = new Visualization(6, "3url", productCard.getSku());

        visualization1 = vdao.add(visualization1);
        visualization2 = vdao.add(visualization2);
        visualization3 = vdao.add(visualization3);

        List<Visualization> visualizations = bl.getVisualListByProduct(productCard);

        assertThat(visualizations, is(notNullValue()));
        assertThat(visualizations, is(anything()));
        assertThat(visualizations.get(0), isA(Visualization.class));
        assertThat(visualization1.getUrl(), is(equalTo("1url")));
        assertThat(visualization2.getType(), is(equalTo(4)));
        assertThat(visualization3.getUrl(), is(equalTo("3url")));

        //Delete from DB
        vdao.deleteAll();
        pdao.delete("222");
        cdao.delete(category.getId());
    }

    @Test
    public void getAttrValuesByProduct() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = pdao.add(productCard);
        AttributeName attributeName = new AttributeName("color");
        andao.add(attributeName);
        AttributeValue attributeValue = new AttributeValue("1", "color", "222");
        avdao.add(attributeValue);
        attributeValue = new AttributeValue("2", "color", "222");
        avdao.add(attributeValue);
        attributeValue = new AttributeValue("3", "color", "222");
        avdao.add(attributeValue);

        List<AttributeValue> list = bl.getAttrValuesByProduct(pdao.get("222"));

        list.forEach(a -> {
            assertThat(a.getValue(), oneOf("1", "2", "3"));
            assertThat(a, is(notNullValue()));
            assertThat(a.getValue(), isA(String.class));
            assertThat(a, isA(AttributeValue.class));
        });
        avdao.deleteAll();
        pdao.delete("222");
        cdao.delete(category.getId());
        andao.delete("color");
    }

    @Test
    public void getAttrValuesByName() throws Exception {
        Category category = new Category("desc", "name", 0);
        category = cdao.add(category);
        ProductCard productCard = new ProductCard("222", "2name", 2222, 34, 45, 4, "xxx", category.getId());
        productCard = pdao.add(productCard);
        AttributeName attributeName = new AttributeName("color");
        andao.add(attributeName);
        AttributeValue attributeValue = new AttributeValue("1", "color", "222");
        avdao.add(attributeValue);
        attributeValue = new AttributeValue("2", "color", "222");
        avdao.add(attributeValue);
        attributeValue = new AttributeValue("3", "color", "222");
        avdao.add(attributeValue);

        List<AttributeValue> list = bl.getAttrValuesByName(andao.get("color"));

        list.forEach(a -> {
            assertThat(a.getValue(), oneOf("1", "2", "3"));
            assertThat(a, is(notNullValue()));
            assertThat(a.getValue(), isA(String.class));
            assertThat(a, isA(AttributeValue.class));
        });
        avdao.deleteAll();
        pdao.delete("222");
        cdao.delete(category.getId());
        andao.delete("color");
    }
}
