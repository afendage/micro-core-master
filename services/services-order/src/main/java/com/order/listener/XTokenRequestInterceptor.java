package com.order.listener;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class XTokenRequestInterceptor implements RequestInterceptor {

    /**
     * 请求拦截器
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("请求拦截器启动");
        requestTemplate.header("X-Token","123456");
    }
}
