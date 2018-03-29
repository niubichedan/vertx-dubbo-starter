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
//        Future<String> future = Future.future();
        VertxService vertxService = reference.get(); // 获取远程服务代理
        vertxService.sayHello();

        FutureAdapter futureAdapter = (FutureAdapter) RpcContext.getContext().getFuture();
        handleReply(futureAdapter).setHandler(res -> {
            System.out.println(res.result());
        });
    }

    private Future<String> handleReply(FutureAdapter futureAdapter) {
        Future<String> future = Future.future();
        ResponseCallback callback = new ResponseCallback() {
            @Override
            public void done(Object response) {
                RpcResult rpcResult = (RpcResult) response;
                System.out.println("****************");
                System.out.println((String) rpcResult.getValue());
                future.complete((String) rpcResult.getValue());
                System.out.println("****************");
            }

            @Override
            public void caught(Throwable exception) {
                System.out.println("****************");
                System.out.println(exception.getMessage());
                future.fail(exception);
                System.out.println("****************");
            }
        };
        futureAdapter.getFuture().setCallback(callback);
        return future;
    }
}
