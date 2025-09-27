package com.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.products.pojo.Product;
import com.order.pojo.Order;
import com.order.service.OrderService;
import com.order.service.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author finger
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired  //一定导入spring-cloud-starter-loadbalancer
    LoadBalancerClient loadBalancerClient;

    @Autowired
    ProductFeignClient productFeignClient;

    @SentinelResource(value="createOrder",blockHandler = "createOrderFallBack")
    @Override
    public Order createOrder(Long productId, Long userId) {
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(new BigDecimal("0"));
        order.setUserId(userId);
        order.setNickName("finger");
        order.setAddress("乌拉拉");
        order.setProductList(null);
        return order;
    }

    //执行兜底回调
    public Order createOrderFallBack(Long productId, Long userId, BlockException e) {
        Order order = new Order();
        order.setId(0L);
        order.setTotalAmount(new BigDecimal(0));
        order.setUserId(userId);
        order.setNickName("未知用户");
        order.setAddress("异常信息"+e.getClass());
        return order;
    }

    /**
     *  DiscoveryClient 远程调用
     * @param productId 商品id
     * @return Product 商品
     */
    @Override
    public Product getProductFromRemote(Long productId){
        //1.获取到商品服务所在的所有机器ip+port
        List<ServiceInstance> instances=discoveryClient.getInstances("services-products");
        ServiceInstance instance=instances.get(0);
        //远程URL
        String url="http://"+instance.getHost()+":"+instance.getPort()+"/product/"+productId;
        log.info("远程请求:{}",url);
        //2.给远程发送请求
        return restTemplate.getForObject(url,Product.class);
    }

    /**
     * LoadBalancerClient 远程调用(完成负载均衡发送请求)
     * @param productId 商品id
     * @return Product 商品
     */
    @Override
    public Product getProductFromRemoteWithLoadBalance(Long productId){
        //1.获取到商品服务所在的所有机器ip+port
        ServiceInstance choose = loadBalancerClient.choose("service-products");
        //远程URL
        String url="http://"+choose.getHost()+":"+choose.getPort()+"/product/"+productId;
        log.info("远程请求:{}",url);
        //2.给远程发送请求
        Product product=restTemplate.getForObject(url,Product.class);
        return product;
    }

    @Override
    public Product getProductWithOpenFien(Long productId) {
        return productFeignClient.getProductById(productId);
    }

    @Override
    public Product getProductWithOpenFien1(Long productId) {
        return productFeignClient.getProductById1(productId);
    }

}