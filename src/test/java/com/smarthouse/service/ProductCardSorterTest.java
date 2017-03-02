package com.smarthouse.service;

import com.smarthouse.dao.ProductCardDao;
import com.smarthouse.pojo.ProductCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static com.smarthouse.service.libs.enums.EnumProductSorter.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/app-config.xml")
public class ProductCardSorterTest {

    @Resource
    private ProductCardDao pdao;
    @Resource
    private ProductCardSorter bl;

    @Test
    public void sortByPopularity() throws Exception {
        List<ProductCard> productCards = pdao.getAll();
        productCards.forEach(a -> System.out.print(a.getLikes() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = bl.productSort(productCards, SORT_BY_POPULARITY);
        productCardsSorted.forEach(a -> System.out.print(a.getLikes() + "  "));
    }

    @Test
    public void sortByUnpopularity() throws Exception {
        List<ProductCard> productCards = pdao.getAll();
        productCards.forEach(a -> System.out.print(a.getDislikes() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = bl.productSort(productCards, SORT_BY_UNPOPULARITY);
        productCardsSorted.forEach(a -> System.out.print(a.getDislikes() + "  "));
    }

    @Test
    public void sortByName() throws Exception {
        List<ProductCard> productCards = pdao.getAll();
        productCards.forEach(a -> System.out.print(a.getName() + " "));
        List<ProductCard> productCardsSorted = bl.productSort(productCards, SORT_BY_NAME);
        System.out.println(productCards.size());
        System.out.println(productCardsSorted.size());
        productCardsSorted.forEach(a -> System.out.print(a.getName() + "  "));
    }

    @Test
    public void sortByNameReversed() throws Exception {
        List<ProductCard> productCards = pdao.getAll();
        productCards.forEach(a -> System.out.print(a.getName() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = bl.productSort(productCards, SORT_BY_NAME_REVERSED);
        productCardsSorted.forEach(a -> System.out.print(a.getName() + "  "));
    }

    @Test
    public void sortByLowerPrice() throws Exception {
        List<ProductCard> productCards = pdao.getAll();
        productCards.forEach(a -> System.out.print(a.getPrice() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = bl.productSort(productCards, SORT_BY_LOW_PRICE);
        productCardsSorted.forEach(a -> System.out.print(a.getPrice() + "  "));
    }

    @Test
    public void sortByHigherPrice() throws Exception {
        List<ProductCard> productCards = pdao.getAll();
        productCards.forEach(a -> System.out.print(a.getPrice() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = bl.productSort(productCards, SORT_BY_HIGH_PRICE);
        productCardsSorted.forEach(a -> System.out.print(a.getPrice() + "  "));
    }

    @Test
    public void sortByAmount() throws Exception {
        List<ProductCard> productCards = pdao.getAll();
        productCards.forEach(a -> System.out.print(a.getAmount() + " "));
        System.out.println();
        List<ProductCard> productCardsSorted = bl.productSort(productCards, SORT_BY_AMOUNT);
        productCardsSorted.forEach(a -> System.out.print(a.getAmount() + "  "));
    }


}
