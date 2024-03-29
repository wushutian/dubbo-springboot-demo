package com.xiaoze.api.service;

import com.xiaze.api.vo.HelloParamVo;

import java.util.concurrent.CompletableFuture;

/**
 * DemoService
 * 服务Api接口类
 * @author xiaoze
 * @date 2018/6/6
 */
public interface DemoService {

    String sayHello(HelloParamVo param);

    CompletableFuture<String> sayAsyncHello(String name);

}
