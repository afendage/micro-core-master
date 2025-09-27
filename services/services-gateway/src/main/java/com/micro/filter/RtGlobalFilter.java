package com.micro.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义 全局过滤器
 * @author finger
 */
@Slf4j
@Component
public class RtGlobalFilter implements GlobalFilter {

/**
 * 网关过滤器方法，用于处理请求的前置和后置逻辑
 * @param exchange 当前的服务器网络交换对象，包含请求和响应信息
 * @param chain 网关过滤器链，用于传递请求到下一个过滤器
 * @return Mono<Void> 表示异步操作的完成
 */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().toString();
        long start=System.currentTimeMillis();
        log.info("请求："+uri+" 开始时间:"+start);
        //---------以上是前置逻辑--------------//
        Mono<Void> filter=chain.filter(exchange)
                .doFinally((result)->{
                    //----------以下是后置逻辑-------//
                    long end=System.currentTimeMillis();
                    log.info("请求"+uri+" 结束时间："+end+" 耗时"+(end-start));
                });//放行
        return filter;
    }
}
