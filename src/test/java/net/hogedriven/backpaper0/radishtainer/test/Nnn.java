package net.hogedriven.backpaper0.radishtainer.test;

import javax.inject.Singleton;
import net.hogedriven.backpaper0.radishtainer.event.Observes;

@Singleton
public class Nnn {

    public Aaa aaa = Aaa.INSTANCE;

    private void handleEvent(@Observes Aaa aaa) {
        this.aaa = aaa;
    }
}
