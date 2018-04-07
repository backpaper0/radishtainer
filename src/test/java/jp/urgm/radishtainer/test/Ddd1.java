package jp.urgm.radishtainer.test;

import javax.inject.Inject;

public class Ddd1 {

    public boolean withAtInjectSuper;

    public boolean noAtInjectSuper;

    @Inject
    public void method1() {
        withAtInjectSuper = true;
    }

    public void method2() {
        noAtInjectSuper = true;
    }
}
