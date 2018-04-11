package io.terminus.demo.spi.impl;

import io.terminus.demo.spi.PrintName;

public class PrintChineseName implements PrintName {
    @Override
    public void print() {
        System.out.println("小志");
    }
}
