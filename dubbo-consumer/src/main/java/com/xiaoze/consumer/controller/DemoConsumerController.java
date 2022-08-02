package com.xiaoze.consumer.controller;

import com.xiaoze.api.service.DemoService;
import com.xiaze.api.vo.HelloParamVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.dubbo.rpc.RpcContext;

/**
 * DemoConsumerController
 * 消费者控制层
 * @author xiaoze
 * @date 2018/6/7
 */
@RestController
public class DemoConsumerController {



    @DubboReference(
            //启动时检查，默认是true， 项目启动时，检测到注册中心挂掉， 项目异常启动不了，
            // false则注册中心挂了，也能启动项目，等待注册中心正常后可以正常调用
            check = true,
            /**
             * 由IP路由
            */
            parameters = {"router", "address"},
            /**
             * 控制客户端对服务掉的调用频率
            */
            actives = 1,

            /**
             * 开启Dubbo参数验证
            */
            validation = "true",
            /**
             * 3.0.9版本服务分组暂不支持通配符*以及多个逗号隔开
            */
            group = "Demo1",
            /**
             * 3.0.9版本不支持merger
            */
//            merger = "true",
            /**
             * 1.Failover Cluster: 重试
             * 失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)。
             * 2.Failfast Cluster: 快速失败
             *  快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录
             * 3.Failsafe Cluster： 安全失败
             * 失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作
             * 4.Failback Cluster：失败自动恢复
             * 失败自动恢复，后台记录失败请求，定时重发。每隔5秒重新发送，通常用于消息通知操作
             * 5.Forking Cluster：并行返回
             * 并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过 forks="2" 来设置最大并行数
             * 6.Broadcast Cluster：广播调用所有提供者，逐个调用
             * 广播调用所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息。
             *
             * 现在广播调用中，可以通过 broadcast.fail.percent 配置节点调用失败的比例，当达到这个比例后，BroadcastClusterInvoker 将不再调用其他节点，直接抛出异常。 broadcast.fail.percent 取值在 0～100 范围内。默认情况下当全部调用失败后，才会抛出异常。 broadcast.fail.percent 只是控制的当失败后是否继续调用其他节点，并不改变结果(任意一台报错则报错)。broadcast.fail.percent 参数 在 dubbo2.7.10 及以上版本生效。
             *
             * Broadcast Cluster 配置 broadcast.fail.percent。
             *
             * broadcast.fail.percent=20 代表了当 20% 的节点调用失败就抛出异常，不再调用其他节点
            */
            cluster = "failover",
            /**
             * 配合failover使用，表示重试次数，重试会重新出发负载均衡，而不是固定一个服务提供者调用
            */
            retries = 2,
            /**
             * 多注册中心
            */
            registry = {"zookeeper2181", "zookeeper2182"},
            /**
             * 版本
            */
            version = "${demo.service.version}",
            /**
             * 多个服务提供方时，提供的负载均衡算法
             * Random LoadBalance
             * 随机，按权重设置随机概率。
             * 在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。
             * RoundRobin LoadBalance
             * 轮询，按公约后的权重设置轮询比率。
             * 存在慢的提供者累积请求的问题，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上。
             * LeastActive LoadBalance
             * 最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差。
             * 使慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大。
             * ConsistentHash LoadBalance
             * 一致性 Hash，相同参数的请求总是发到同一提供者。
             * 当某一台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其它提供者，不会引起剧烈变动。
             * 算法参见：http://en.wikipedia.org/wiki/Consistent_hashing
             * 缺省只对第一个参数 Hash，如果要修改，请配置 <dubbo:parameter key="hash.arguments" value="0,1" />
             * 缺省用 160 份虚拟节点，如果要修改，请配置 <dubbo:parameter key="hash.nodes" value="320" />
            */
            loadbalance = "random",
            timeout = 10000,
    mock = "com.xiaoze.consumer.mock.DemoMockServiceImpl",

            /**
             * 方法级别设置
             * async=true 方法以异步的方式运行
             */
    methods = {@Method(name = "sayAsyncHello", async = true)},
            /**
             * 连接粘连-始终只使用第一次连接到的服务
            */
            sticky = true
    )
    private DemoService demoService;

    public static AtomicInteger cri = new AtomicInteger();

    @RequestMapping("/sayHello")
    public String sayHello(String name) {
        HelloParamVo helloParamVo = new HelloParamVo();
        helloParamVo.setName(name);
        helloParamVo.setCount(cri.incrementAndGet());
//        Address address = new Address("10.220.47.253", 20880);
//        RpcContext.getContext().setObjectAttachment("address", address);
        return demoService.sayHello(helloParamVo);
    }

    @RequestMapping("/asyncSayHello")
    public String asyncSayHello(String name) {
        CompletableFuture<String> future = demoService.sayAsyncHello("123");
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }




}
