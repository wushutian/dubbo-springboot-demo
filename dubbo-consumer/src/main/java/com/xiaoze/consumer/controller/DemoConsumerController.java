package com.xiaoze.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaoze.api.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoConsumerController
 * 消费者控制层
 * @author xiaoze
 * @date 2018/6/7
 */
@RestController
public class DemoConsumerController {

    @DubboReference(cluster = "failover", retries = 2, version = "${demo.service.version}", loadbalance = "random", timeout = 10000,
    mock = "com.xiaoze.consumer.mock.DemoMockServiceImpl")
    private DemoService demoService;

    @RequestMapping("/sayHello/{name}")
    public String sayHello(@PathVariable("name") String name) {
        return demoService.sayHello(name);
    }

}