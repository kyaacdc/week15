package com.smarthouse.service.libs.enums;


import com.smarthouse.pojo.ProductCard;

import java.util.Comparator;
import java.util.function.Function;

public enum EnumProductSorter implements Function<ProductCard, Object>, Comparator<ProductCard> {

    SORT_BY_NAME {
        @Override
        public int compare(ProductCard o1, ProductCard o2) {
            return o1.getName().compareTo(o2.getName());
        }
        @Override
        public String apply(ProductCard productCard) {
            return productCard.getName();
        }
    },

    SORT_BY_NAME_REVERSED {
        @Override
        public int compare(ProductCard o1, ProductCard o2) {
            return o2.getName().compareTo(o1.getName());
        }
        @Override
        public String apply(ProductCard productCard) {
            return productCard.getName();
        }
    },

    SORT_BY_POPULARITY {
        @Override
        public int compare(ProductCard o1, ProductCard o2) {
            if (o2.getLikes() > o1.getLikes())
                return 1;
            else if (o2.getLikes() < o1.getLikes())
                return -1;
            else return 1;
        }
        @Override
        public Integer apply(ProductCard productCard) {
            return productCard.getLikes();
        }
    },

    SORT_BY_UNPOPULARITY {
        @Override
        public int compare(ProductCard o1, ProductCard o2) {
            if (o2.getDislikes() > o1.getDislikes())
                return 1;
            else if (o2.getDislikes() < o1.getDislikes())
                return -1;
            else return 1;
        }
        @Override
        public Integer apply(ProductCard productCard) {
            return productCard.getDislikes();
        }
    },

    SORT_BY_LOW_PRICE {
        @Override
        public int compare(ProductCard o1, ProductCard o2) {
            if (o1.getPrice() > o2.getPrice())
                return 1;
            else if (o1.getPrice() < o2.getPrice())
                return -1;
            else return 1;
        }
        @Override
        public Integer apply(ProductCard productCard) {
            return productCard.getPrice();
        }
    },

    SORT_BY_HIGH_PRICE {
        @Override
        public int compare(ProductCard o1, ProductCard o2) {
            if (o2.getPrice() > o1.getPrice())
                return 1;
            else if (o2.getPrice() < o1.getPrice())
                return -1;
            else return 1;
        }
        @Override
        public Integer apply(ProductCard productCard) {
            return productCard.getPrice();
        }
    },

    SORT_BY_AMOUNT {
        @Override
        public int compare(ProductCard o1, ProductCard o2) {
            if (o2.getAmount() > o1.getAmount())
                return 1;
            else if (o2.getAmount() < o1.getAmount())
                return -1;
            else return 1;
        }
        @Override
        public Integer apply(ProductCard productCard) {
            return productCard.getAmount();
        }
    }
}
