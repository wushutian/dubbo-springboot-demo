package com.xiaoze.provider.service.impl;

import com.xiaoze.api.service.DemoService;
import com.xiaze.api.vo.HelloParamVo;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.concurrent.CompletableFuture;

/**
 * DemoServiceImpl
 * 服务提供类
 * @author xiaoze
 * @date 2018/6/7
 */
@DubboService(cluster = "failover", validation = "true", group = "Demo2", retries = 2, version = "${demo.service.version}", actives = 1,
        methods = {

}, registry = {"zookeeper2181", "zookeeper2182"})
public class DemoServiceImpl2 implements DemoService {

    @Override
    public String sayHello(HelloParamVo param) {
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, " + param.getName() + " count "+ param.getCount() +" (from Spring Boot)2";
    }

    @Override
    public CompletableFuture<String> sayAsyncHello(String name) {
        // 建议为supplyAsync提供自定义线程池，避免使用JDK公用线程池
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "async response from provider.";
        });
    }
}
