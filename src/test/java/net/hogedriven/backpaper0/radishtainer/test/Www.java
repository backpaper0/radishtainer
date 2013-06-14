package net.hogedriven.backpaper0.radishtainer.test;

import javax.inject.Inject;

public class Www {

    public final static Www INSTANCE = new Www();
    @Inject
    public Aaa aaa = Aaa.INSTANCE;
}
