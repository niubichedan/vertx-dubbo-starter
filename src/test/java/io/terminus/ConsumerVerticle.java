package io.terminus;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import io.terminus.service.VertxService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import java.util.concurrent.ExecutionException;


/**
 * @author: yuhang
 * @Date: 27/03/2018 18:37
 */
public class ConsumerVerticle extends AbstractVerticle {
    @Override
    public void start() throws ExecutionException, InterruptedException {
        System.out.println("verticle start!");
//当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("vertxServiceUser");

        //连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");

        //注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

        //引用远程服务
        ReferenceConfig<VertxService> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(VertxService.class);
        reference.setVersion("1.0.0");
        reference.setAsync(true);
        reference.setTimeout(3000);
        VertxService vertxService = reference.get(); // 获取远程服务代理
        String originalName = Thread.currentThread().getName();
        System.out.println(originalName);
//        vertx.setPeriodic(1000, timer -> {
//            vertxService.sayHello();
//            //下面是异步的例子
//            FutureAdapter futureAdapter = (FutureAdapter) RpcContext.getContext().getFuture();
//            handleReply(futureAdapter).setHandler(res -> {
//                System.out.println("****************");
//                String name = Thread.currentThread().getName();
//                System.out.println(name + "::" + name.equals(originalName));
//                System.out.println(res.result());
//                System.out.println("****************");
//            });
//            System.out.println("****************");
//            System.out.println("我会先打印！");
//            System.out.println("****************");
//        });
        //下面是同步的例子
        vertxService.sayHello();
        System.out.println("****************");
        System.out.println("你会看到阻塞线程告警！");
        System.out.println((String) RpcContext.getContext().getFuture().get());
    }

    private Future<String> handleReply(FutureAdapter futureAdapter) {
        Future<String> future = Future.future();
        ResponseCallback callback = new ResponseCallback() {
            @Override
            public void done(Object response) {
                context.runOnContext(res2 -> {
                    RpcResult rpcResult = (RpcResult) response;
                    future.complete((String) rpcResult.getValue());
                });
            }

            @Override
            public void caught(Throwable exception) {
                context.runOnContext(res2 -> {
                    System.out.println("****************");
                    future.fail(exception);
                    System.out.println("****************");
                });
            }
        };
        futureAdapter.getFuture().setCallback(callback);
        return future;
    }
}
