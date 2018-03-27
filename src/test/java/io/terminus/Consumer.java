package io.terminus;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import io.terminus.service.VertxService;

import java.io.IOException;

/**
 * @author: yuhang
 * @Date: 27/03/2018 17:52
 */
public class Consumer {
    public static void main(String[] args) throws IOException {
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
        reference.setTimeout(3000);

        //和本地Bean一样使用Service
        VertxService vertxService = reference.get(); // 获取远程服务代理
        System.out.println(vertxService.sayHello());
    }
}
