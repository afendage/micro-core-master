package com.order.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 获取 nacos 配置变更监听器
 *
 * @author finger
 */
@Component
public class ConfigChangeListener implements ApplicationListener<EnvironmentChangeEvent> {

    @Autowired
    private Environment environment;

    /**
     * (获取具体修改的元素)
     * @param event 事件
     */
    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().forEach(key ->
            System.out.println("变更键: " + key + ", 新值: " + environment.getProperty(key))
        );
    }

}
