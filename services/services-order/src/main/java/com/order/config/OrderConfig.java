package com.order.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderConfig {

    @LoadBalanced   //注解式负载均衡
    @Bean
    public RestTemplate restTemplate(){
        //restTemplate用于给远程发送请求
        return new RestTemplate();
    }

    // 日志级别
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 远程调用超时失败后，还可以进行多次尝试(不传参数就是间隔100毫秒，最大间隔1秒，最多尝试5次)
     * @return
     */
    @Bean
    Retryer retryer(){
        return new Retryer.Default();
    }


}
