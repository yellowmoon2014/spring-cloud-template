package com.hy.template.user.controller;

import com.hy.template.user.api.UserInfoServiceApi;
import com.netflix.appinfo.EurekaInstanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("userInfoService")
public class UserInfoController implements UserInfoServiceApi {

    protected Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Resource
    private EurekaInstanceConfig eurekaInstanceConfig;

    @Value("${server.port}")
    private int serverPort = 0;

    @GetMapping("hello")
    @Override
    public String hello() {
        this.logger.info("/hello, instanceId:{}, host:{}", eurekaInstanceConfig.getInstanceId(), eurekaInstanceConfig.getHostName(false));
        return "Hello, Spring Cloud! My port is " + String.valueOf(serverPort);
    }


}
