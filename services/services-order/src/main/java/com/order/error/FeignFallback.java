package com.order.error;

import com.order.service.ProductFeignClient;
import com.products.pojo.Product;
import org.springframework.stereotype.Component;

/**
 * 兜底服务
 */
@Component
public class FeignFallback  implements ProductFeignClient {


    @Override
    public Product getProductById(Long id) {
        Product product = new Product();
        product.setMsg("error:服务异常");
        return product;
    }

    @Override
    public Product getProductById1(Long id) {
        Product product = new Product();
        product.setMsg("error1:服务异常");
        return product;
    }
}
