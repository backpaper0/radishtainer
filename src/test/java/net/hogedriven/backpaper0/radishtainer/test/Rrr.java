package net.hogedriven.backpaper0.radishtainer.test;

import java.util.concurrent.CountDownLatch;
import javax.inject.Singleton;

@Singleton
public class Rrr {

    public static final CountDownLatch start = new CountDownLatch(1);

    public Rrr() throws InterruptedException {
        start.await();
    }
}
