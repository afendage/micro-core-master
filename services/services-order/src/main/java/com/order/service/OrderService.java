package com.order.service;

import com.products.pojo.Product;
import com.order.pojo.Order;

public interface OrderService {

    Order createOrder(Long productId,Long userId);

    Product getProductFromRemote(Long productId);

    Product getProductFromRemoteWithLoadBalance(Long productId);

    Product getProductWithOpenFien(Long productId);

    Product getProductWithOpenFien1(Long productId);
}
