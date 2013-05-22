package net.hogedriven.backpaper0.radishtainer.test;

import java.util.concurrent.atomic.AtomicInteger;

public class Aaa {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static final Aaa INSTANCE = new Aaa();

    public final int count;

    public Aaa() {
        count = COUNTER.incrementAndGet();
    }

    @Override
    public String toString() {
        return "Aaa(" + count + ")";
    }
}
