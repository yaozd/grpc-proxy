package com.codahale.grpcproxy;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: yaozh
 * @Description:
 */
public class TerminationUtil {
    private CountDownLatch latch = new CountDownLatch(1);
    public void blockUntilShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        latch.countDown();
    }
}
