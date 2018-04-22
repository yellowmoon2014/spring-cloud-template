package com.hy.template.order.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("orderInfoServiceApi")
public interface OrderInfoServiceApi {
    @GetMapping("hello")
    String hello(@RequestParam("orderId") Long orderId);
}
