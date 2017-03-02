package com.smarthouse.service.libs.util;

import com.smarthouse.dao.CustomerDao;
import com.smarthouse.dao.ProductCardDao;
import com.smarthouse.pojo.ProductCard;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.NoSuchElementException;

public class Checker {

    private static ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
    private static ProductCardDao pdao  = (ProductCardDao) ac.getBean("productCardDao");
    private static CustomerDao cdao  = (CustomerDao) ac.getBean("customerDao");

    //Return product availabitity in storehouse by amount
    public static boolean isRequiredAmountOfProductCardAvailable(String sku, int amount){

        ProductCard productCard = pdao.get(sku);

        int productCardAmount = productCard.getAmount();

        return amount <= productCardAmount;
    }

    //Return product availabitity in storehouse
    public static boolean isProductAvailable(String sku) {
        return pdao.get(sku).getAmount() > 0;
    }

    //Return is customer exist
    public static boolean isCustomerExist(String email) {
        return cdao.isEmailExist(email);
    }
}
