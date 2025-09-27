package com.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author finger
 */
@Component
@ConfigurationProperties(prefix = "order")      //获取配置文件的order 开头的所有熟悉(如：order.timeout)
@Data
public class OrderConfigInfo {
    private String timeout;
    // 配置有横线就用 驼峰命名
    private String autoConfirm;


}
