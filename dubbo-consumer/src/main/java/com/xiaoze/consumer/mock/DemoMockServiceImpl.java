package com.xiaoze.consumer.mock;

import com.xiaoze.api.service.DemoService;
import com.xiaze.api.vo.HelloParamVo;

import java.util.concurrent.CompletableFuture;

public class DemoMockServiceImpl implements DemoService {
    @Override
    public String sayHello(HelloParamVo param) {
        return "我服务出问题了";
    }

    @Override
    public CompletableFuture<String> sayAsyncHello(String name) {
        return null;
    }

}
