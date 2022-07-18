package com.xiaoze.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaoze.api.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * DemoServiceImpl
 * 服务提供类
 * @author xiaoze
 * @date 2018/6/7
 */
@DubboService(cluster = "failover", retries = 2, version = "${demo.service.version}", actives = 2,
        methods = {

}, registry = {"zookeeper2181", "zookeeper2182"})
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name + " (from Spring Boot)";
    }
}
