package net.hogedriven.backpaper0.radishtainer.test;

import javax.inject.Inject;

public class Fff {

    //field injection
    @Inject
    private Aaa field = Aaa.INSTANCE;

    //method injection
    private Aaa method = Aaa.INSTANCE;

    @Inject
    private void setMethod(Aaa method) {
        this.method = method;
    }

    // getter
    public Aaa getField() {
        return field;
    }

    public Aaa getMethod() {
        return method;
    }
}
