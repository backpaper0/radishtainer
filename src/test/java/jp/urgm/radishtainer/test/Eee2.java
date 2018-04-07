package jp.urgm.radishtainer.test;

public class Eee2 extends Eee1 {

    public boolean privateMethodSub;

    public boolean packagePrivateMethodSub;

    public boolean protectedMethodSub;

    public boolean publicMethodSub;

    private void privateMethod() {
        privateMethodSub = true;
    }

    @Override
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
