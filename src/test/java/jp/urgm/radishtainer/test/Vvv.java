package jp.urgm.radishtainer.test;

import javax.inject.Inject;

public abstract class Vvv {

    @Inject
    Object injectableField;

    Object noAtInjectField;

    @Inject
    final Object finalField = null;

    @Inject
    void injectableMethod() {
    }

    void noAtInjectMethod() {
    }

    @Inject
    abstract void abstractMethod();
}
