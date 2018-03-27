package io.terminus;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import io.terminus.service.VertxService;
import io.terminus.service.impl.VertxServiceImpl;

import java.io.IOException;

/**
 * @author: yuhang
 * @Date: 27/03/2018 15:02
 */
public class Provider {
    public static void main(String[] args) throws IOException {
//服务实现
        VertxService vertxService = new VertxServiceImpl();

        //当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("VertxService");

        //连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");//注册中心地址

        //服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(20880);
        protocol.setThreads(200);

        //注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口

        //服务提供者暴露服务配置
        ServiceConfig<VertxService> service = new ServiceConfig<>();
        service.setApplication(application);
        service.setRegistry(registry);
        service.setProtocol(protocol);
        service.setInterface(VertxService.class);
        service.setRef(vertxService);
        service.setVersion("1.0.0");

        //暴露及注册服务
        service.export();
        System.out.println("服务已暴露！");
        System.in.read(); // 按任意键退出
    }
}
