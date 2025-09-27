package com.finger.aa;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "services-products",
        contextId = "AAA" // 确保唯一性
)
public interface AAA {

    void aa();
}
