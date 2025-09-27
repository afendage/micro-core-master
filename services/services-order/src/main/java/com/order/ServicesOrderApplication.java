package com.order;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * @author finger
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ServicesOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicesOrderApplication.class, args);
    }

    /**
     *  1. 项目启动就监听配置文件变化
     *  2. 发生变化后拿到变化值
     *  3. 发送邮件
     * @param nacosConfigManager
     * @return
     */
    @Bean
    ApplicationRunner applicationRunner(NacosConfigManager nacosConfigManager){
        return  args-> {
            ConfigService configService= nacosConfigManager.getConfigService();
            configService.addListener("services-products.yml",
                    "DEFAULT_GROUP", new Listener() {
                        @Override
                        public Executor getExecutor() {
                            return Executors.newFixedThreadPool(4);
                        }
                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            System.out.println("变化的配置信息：\n"+configInfo);
                            System.out.println("邮件通知...");
                        }
                    });
            System.out.println("启动");
        };
    }

}