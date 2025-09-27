package com.order.pojo;

import com.products.pojo.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order {

    private Long id;
    private BigDecimal totalAmount;
    private Long userId;
    private  String nickName;
    private  String address;
    private List<Product> productList;

}
