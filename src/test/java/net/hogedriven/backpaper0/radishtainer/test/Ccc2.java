package net.hogedriven.backpaper0.radishtainer.test;

import javax.inject.Inject;

public class Ccc2 extends Ccc1 {

    // field injection
    @Inject
    private Aaa subField;

    // method injection
    private Aaa subMethod;

    @Inject
    public void setSubMethod(Aaa subMethod) {
        this.subMethod = subMethod;
    }

    // getter
    public Aaa getSubField() {
        return subField;
    }

    public Aaa getSubMethod() {
        return subMethod;
    }
}
