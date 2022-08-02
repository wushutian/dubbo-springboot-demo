package com.xiaoze.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaoze.api.service.DemoService;
import com.xiaze.api.vo.HelloParamVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * DemoServiceImpl
 * 服务提供类
 * @author xiaoze
 * @date 2018/6/7
 */
@DubboService(cluster = "failover",  group = "Demo1", retries = 2, version = "${demo.service.version}",
        methods = {

}, registry = {"zookeeper2181", "zookeeper2182"})
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(HelloParamVo param) {
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, " + param.getName() + " count "+ param.getCount() +" (from Spring Boot)";
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
