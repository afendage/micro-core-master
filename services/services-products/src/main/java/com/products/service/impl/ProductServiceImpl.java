package com.products.service.impl;

import com.products.pojo.Product;
import com.products.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author finger
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Product getProductById(Long productId) {
        Product product=new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("99"));
        product.setProductName("苹果"+productId);
        product.setNum(2);
        return product;
    }
}
