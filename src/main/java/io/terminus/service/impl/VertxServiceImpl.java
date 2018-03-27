package io.terminus.service.impl;

import io.terminus.service.VertxService;

/**
 * @author: yuhang
 * @Date: 27/03/2018 15:16
 */
public class VertxServiceImpl implements VertxService {

    @Override
    public String sayHello() {
        try {
            System.out.println("i will sleep 2000 ms");
            Thread.sleep(2500);
            System.out.println("sleep end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello Vertx!";
    }
}
