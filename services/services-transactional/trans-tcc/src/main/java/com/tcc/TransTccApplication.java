package com.tcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author finger
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TransTccApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransTccApplication.class, args);
    }
}