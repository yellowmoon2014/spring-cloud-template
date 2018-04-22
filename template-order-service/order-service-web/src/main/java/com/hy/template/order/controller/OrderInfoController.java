package com.hy.template.order.controller;

import com.hy.template.order.api.OrderInfoServiceApi;
import com.hy.template.user.api.UserInfoServiceApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("orderInfoService")
public class OrderInfoController implements OrderInfoServiceApi {

    @Resource
    private UserInfoServiceApi userInfoServiceApi;

    @GetMapping("hello")
    @Override
    public String hello(@RequestParam("orderId") Long orderId) {
        String userInfo = userInfoServiceApi.hello();
        String result = userInfo + orderId;
        return result;
    }

}
