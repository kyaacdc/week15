package com.smarthouse.service;

import com.smarthouse.dao.AttributeValueDao;
import com.smarthouse.dao.CategoryDao;
import com.smarthouse.dao.ProductCardDao;
import com.smarthouse.dao.VisualizationDao;
import com.smarthouse.pojo.*;
import com.smarthouse.service.libs.enums.EnumSearcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.smarthouse.service.libs.enums.EnumSearcher.FIND_ALL;

public class ProductCardGetter {

    private ProductCardDao productCardDao;
    private CategoryDao categoryDao;
    private VisualizationDao visualizationDao;
    private AttributeValueDao attributeValueDao;


    public ProductCardGetter() {}

    public ProductCardGetter(ProductCardDao productCardDao, CategoryDao categoryDao,
                             VisualizationDao visualizationDao, AttributeValueDao attributeValueDao) {
        this.productCardDao = productCardDao;
        this.categoryDao = categoryDao;
        this.visualizationDao = visualizationDao;
        this.attributeValueDao = attributeValueDao;
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
}
