package io.terminus;

import io.vertx.core.Vertx;

/**
 * @author: yuhang
 * @Date: 27/03/2018 18:38
 */
public class Start {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ConsumerVerticle.class.getName());
    }
}
