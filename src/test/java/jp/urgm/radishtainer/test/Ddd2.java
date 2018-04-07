package jp.urgm.radishtainer.test;

import javax.inject.Inject;

public class Ddd2 extends Ddd1 {

    public boolean withAtInjectSub;

    public boolean noAtInjectSub;

    @Override
    public void method1() {
        noAtInjectSub = true;
    }

    @Inject
    @Override
    public void method2() {
        withAtInjectSub = true;
    }
}
