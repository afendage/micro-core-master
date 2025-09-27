package com.products.controller;

import com.products.pojo.Product;
import com.products.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Autowired
    ProductService productService;
    //查询商品
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long productId, HttpServletRequest request){
        System.out.println("token: "+request.getHeader("X-Token"));
        Product product=productService.getProductById(productId);
        return product;
    }

    /**
     * 熔断测试
     * @param productId
     * @return
     */
    @GetMapping("/product1/{id}")
    public Product getProduct(@PathVariable("id") Long productId){
        System.out.println("已请求-未熔断========");
        Product product=productService.getProductById(productId);
        int i=10/0;
        return product;
    }

}
