package com.xiaoze.consumer.mock;

import com.xiaoze.api.service.DemoService;

public class DemoMockServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "我服务出问题了";
    }
}
