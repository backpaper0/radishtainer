package net.hogedriven.backpaper0.radishtainer.test;

import javax.inject.Singleton;
import net.hogedriven.backpaper0.radishtainer.event.Observes;

@Singleton
public class Ppp {

    public Aaa event = Aaa.INSTANCE;

    public Aaa injected = Aaa.INSTANCE;

    public Aaa withQualifier = Aaa.INSTANCE;

    void handle(@Observes Aaa event, Aaa injected, @Iii1 Aaa withQualifier) {
        this.event = event;
        this.injected = injected;
        this.withQualifier = withQualifier;
    }
}
