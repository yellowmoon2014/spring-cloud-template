package com.hy.template.user.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "USER-SERVICE", path = "userInfoService")
public interface UserInfoServiceApi {
    @GetMapping("hello")
    String hello();
}
