package com.order.service;

import com.order.error.FeignFallback;
import com.products.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author finger
 */
@FeignClient(
        value = "services-products",
        contextId = "ProductFeignClient", // 确保唯一性
        fallback = FeignFallback.class
)
public interface ProductFeignClient {
    @GetMapping("/api/products/product/{id}")
    Product getProductById(@PathVariable Long id);

    @GetMapping("/api/products/product1/{id}")
    Product getProductById1(@PathVariable Long id);

}
