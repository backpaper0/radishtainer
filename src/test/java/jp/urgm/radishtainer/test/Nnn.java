package jp.urgm.radishtainer.test;

import javax.inject.Singleton;

import jp.urgm.radishtainer.event.Observes;

@Singleton
public class Nnn {

    public Aaa aaa = Aaa.INSTANCE;

    private void handleEvent(@Observes Aaa aaa) {
        this.aaa = aaa;
    }
}
