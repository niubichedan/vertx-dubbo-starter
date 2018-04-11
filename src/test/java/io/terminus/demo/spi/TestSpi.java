package io.terminus.demo.spi;


import org.junit.Test;

import java.util.ServiceLoader;

public class TestSpi {

    @Test
    public void test() {
        ServiceLoader<PrintName> loaders = ServiceLoader.load(PrintName.class);
        for (PrintName loader : loaders) {
            loader.print();
        }
    }
}
