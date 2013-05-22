package net.hogedriven.backpaper0.radishtainer.test;

import javax.inject.Inject;

public class Ccc1 {

    // field injection
    @Inject
    private Aaa superField;

    // method injection
    private Aaa superMethod;

    @Inject
    public void setSuperMethod(Aaa superMethod) {
        this.superMethod = superMethod;
    }

    // getter
    public Aaa getSuperField() {
        return superField;
    }

    public Aaa getSuperMethod() {
        return superMethod;
    }
}
