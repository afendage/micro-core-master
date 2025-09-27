package com.products.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private Long id;
    private BigDecimal price;
    private String productName;
    private int num;
    private String msg;

}
