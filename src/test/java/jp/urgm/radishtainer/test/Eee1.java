package jp.urgm.radishtainer.test;

import javax.inject.Inject;

public class Eee1 {

    public boolean privateMethodSuper;

    public boolean packagePrivateMethodSuper;

    public boolean protectedMethodSuper;

    public boolean publicMethodSuper;

    @Inject
    private void privateMethod() {
        privateMethodSuper = true;
    }

    @Inject
    void packagePrivateMethod() {
        packagePrivateMethodSuper = true;
    }

    @Inject
    protected void protectedMethod() {
        protectedMethodSuper = true;
    }

    @Inject
    public void publicMethod() {
        publicMethodSuper = true;
    }
}
