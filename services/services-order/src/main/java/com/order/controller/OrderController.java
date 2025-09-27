package com.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.order.config.OrderConfigInfo;
import com.order.pojo.Order;
import com.order.service.OrderService;
import com.order.service.ProductFeignClient;
import com.products.pojo.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RefreshScope   //自动刷新配置
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    //创建订单
    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId, @RequestParam("productId")Long productId){
        Order order = orderService.createOrder(productId,userId);
        return order;
    }

    //秒杀创建订单
    @SentinelResource(value = "sekill",fallback = "sekillFallback")
    @GetMapping("/sekill")
    public Order sekill(@RequestParam("userId") Long userId,@RequestParam("productId")Long productId){
        Order order = orderService.createOrder(productId,userId);
        order.setId(Long.MAX_VALUE);
        return order;
    }
    public Order sekillFallback(Long userId, Long productId,Throwable exception){
        System.out.println("sekillFallback...");
        Order order=new Order();
        order.setId(productId);
        order.setUserId(userId);
        order.setAddress("异常信息："+exception.getClass());
        return order;
    }

    @GetMapping("/getProduct")
    public Product getProductFromRemote(@RequestParam("productId")Long productId){
        return orderService.getProductFromRemote(productId);
    }

    @GetMapping("/getProductByLoadBalance")
    public Product getProductByLoadBalance( @RequestParam("productId")Long productId){
        return orderService.getProductFromRemoteWithLoadBalance(productId);
    }

    @GetMapping("/getProductWithOpenFien")
    public Product getProductWithOpenFien(Long productId) {
        return orderService.getProductWithOpenFien(productId);
    }

    @GetMapping("/getProductWithOpenFien1")
    public Product getProductWithOpenFien1(Long productId) {
        return orderService.getProductWithOpenFien1(productId);
    }

    @GetMapping("/timeOutTest")
    public Object timeOutTest(){
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Value("${order.timeout}")
    String orderTimeout;
    @Value("${order.auto-confirm}")
    String orderAutoConfirm;
    @Autowired
    OrderConfigInfo orderConfigInfo;

    @GetMapping("/getConfigInfo")
    public Object getConfigInfo(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("timeout",orderTimeout);
        map.put("confirm",orderAutoConfirm);
        map.put("configInfo",orderConfigInfo);
        return map;
    }

    @GetMapping("writeDb")
    public String writeDb(){
        return "writeDb";
    }
    @GetMapping("readDb")
    public String readDb(){
        return "readDb";
    }
}
