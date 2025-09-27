package com.order.error;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.pojo.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

/**
 * ⾃定义异常处理类
 * @author finger
 */
@Component
public class OrderExceptionHandler implements BlockExceptionHandler {

    private ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String resource, BlockException e) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        R error=R.error(500,resource+"被Sentinel限制了,原因："+e.getClass());
        //将对象转成字符串
        String json=objectMapper.writeValueAsString(error);
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
