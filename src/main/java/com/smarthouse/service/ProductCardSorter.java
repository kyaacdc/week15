package com.smarthouse.service;

import com.smarthouse.pojo.ProductCard;
import com.smarthouse.service.libs.enums.EnumProductSorter;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCardSorter {

    public ProductCardSorter() {
    }

    /**
     *   Method productSort(List<ProductCard> list, EnumProductSorter criteria)
     *  for sort items list by different custom parameters
     *  @param criteria function to apply sort criteria, like one of:
     *                  SORT_BY_NAME
     *                  SORT_BY_REVERSED
     *                  SORT_BY_AMOUNT
     *                  SORT_BY_LIKES (_DISLIKES)
     *                  SORT_BY_PRICE (_REVERSED)
     *  @return sorted List<ProductCard>
     *  This is my old release sortByName method without enum class,
     *  but to many copy/paste code was,w ith 8 sorting methods, now only 1
     *  public List<ProductCard> sortByNameReversed(List<ProductCard> list){
     *     return list.stream()
     *             .sorted(Comparator.comparing(ProductCard::getName).reversed())
     *              .collect(Collectors.toList());
     *  }
     */
    public List<ProductCard> productSort(List<ProductCard> list, EnumProductSorter criteria){
        return list.stream()
                .sorted(criteria)
                .collect(Collectors.toList());
    }

}
