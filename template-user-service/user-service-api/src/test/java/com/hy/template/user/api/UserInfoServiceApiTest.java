package com.hy.template.user.api;

import com.hy.template.user.BaseTest;
import com.hy.template.user.util.FeignCloud;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserInfoServiceApiTest extends BaseTest {

    private UserInfoServiceApi userInfoServiceApi;


    @Before
    public void setUp() throws Exception {
        userInfoServiceApi = FeignCloud.getInstance(UserInfoServiceApi.class, URL);
    }

    @Test
    public void hello() {
        String rel = userInfoServiceApi.hello();
        Assert.assertEquals("Hello, Spring Cloud! My port is 8080", rel);
    }
}
