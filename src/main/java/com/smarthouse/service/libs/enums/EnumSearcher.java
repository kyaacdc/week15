package com.smarthouse.service.libs.enums;

import com.smarthouse.dao.CategoryDao;
import com.smarthouse.dao.ProductCardDao;
import com.smarthouse.pojo.ProductCard;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

public enum EnumSearcher {

    FIND_ALL,
    FIND_BY_NAME,
    FIND_BY_SKU,
    FIND_IN_PROD_DESC,
    FIND_IN_CATEGORY_NAME,
    FIND_IN_CATEGORY_DESC;

    private ProductCardDao productCardDao;
    private CategoryDao categoryDao;



    EnumSearcher() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        productCardDao = (ProductCardDao) ac.getBean("productCardDao");
        categoryDao = (CategoryDao) ac.getBean("categoryDao");
    }

    private Set<ProductCard> set;

    public  Set<ProductCard> findAll (String criteria)
    {
        Set<ProductCard> result = new LinkedHashSet<>();
        List<ProductCard> all = productCardDao.getAll();

        set = findBySku(criteria, all);
        result.addAll(set);
        set = findByName(criteria, all);
        result.addAll(set);
        set = findInProductDescription(criteria, all);
        result.addAll(set);
        //set = findInCategoryDescription(criteria, all);
        //result.addAll(set);
        //set = findInCategoryName(criteria, all);
        //result.addAll(set);

        return result;
    }

    public  Set<ProductCard> findByPlace
            (String criteria, List<ProductCard> all, EnumSearcher placeForFind)
    {
        switch (placeForFind) {
            case FIND_BY_SKU:
                return findBySku(criteria, all);
            case FIND_BY_NAME:
                return findByName(criteria, all);
            case FIND_IN_PROD_DESC:
                return findInProductDescription(criteria, all);
            case FIND_IN_CATEGORY_NAME:
                return findInCategoryName(criteria, all);
            case FIND_IN_CATEGORY_DESC:
                return findInCategoryDescription(criteria, all);
            default:
                throw new NoSuchElementException();
        }
    }

    public Set<ProductCard> findBySku(String criteria, List<ProductCard> all){
        set = new LinkedHashSet<>();
        for(ProductCard p: all) {
            String sku = p.getSku();
            if (sku.equals(criteria))
                set.add(p);
        }
        return set;
    }

    public Set<ProductCard> findByName(String criteria, List<ProductCard> all){
        set = new LinkedHashSet<>();
        for(ProductCard p: all) {
            String name = p.getName();
            if (name.equals(criteria))
                set.add(p);
        }
        return set;
    }

    public Set<ProductCard> findInProductDescription(String criteria, List<ProductCard> all){
        set = new LinkedHashSet<>();
        for(ProductCard p: all){
            String name = p.getProductDescription();
            if(name.equals(criteria))
                set.add(p);
        }
        return set;
    }

    public Set<ProductCard> findInCategoryName(String criteria, List<ProductCard> all){
        set = new LinkedHashSet<>();
        for(ProductCard p: all){
            String name = categoryDao.get(p.getCategoryId()).getName();
            if(name.equals(criteria))
                set.add(p);
        }
        return set;
    }

    public Set<ProductCard> findInCategoryDescription(String criteria, List<ProductCard> all){
        set = new LinkedHashSet<>();
        for(ProductCard p: all){
            String name = categoryDao.get(p.getCategoryId()).getDescription();
            if(name.equals(criteria))
                set.add(p);
        }
        return set;
    }
}
