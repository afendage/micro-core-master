package com.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author finger
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient  // 开启服务发现功能
public class ServicesProductsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicesProductsApplication.class, args);
    }
}