package net.hogedriven.backpaper0.radishtainer.test.sub;

import net.hogedriven.backpaper0.radishtainer.test.Eee1;

public class Eee3 extends Eee1 {

    public boolean privateMethodSub;

    public boolean packagePrivateMethodSub;

    public boolean protectedMethodSub;

    public boolean publicMethodSub;

    private void privateMethod() {
        privateMethodSub = true;
    }

    void packagePrivateMethod() {
        packagePrivateMethodSub = true;
    }

    @Override
    protected void protectedMethod() {
        protectedMethodSub = true;
    }

    @Override
    public void publicMethod() {
        publicMethodSub = true;
    }
}
