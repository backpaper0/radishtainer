package jp.urgm.radishtainer.test;

import javax.inject.Inject;

public class Bbb {

    // field injection
    @Inject
    private Aaa privateField = Aaa.INSTANCE;

    @Inject
    Aaa packagePrivateField = Aaa.INSTANCE;

    @Inject
    protected Aaa protectedField = Aaa.INSTANCE;

    @Inject
    public Aaa publicField = Aaa.INSTANCE;

    // method injection
    private Aaa privateMethod = Aaa.INSTANCE;

    private Aaa packagePrivateMethod = Aaa.INSTANCE;

    private Aaa protectedMethod = Aaa.INSTANCE;

    private Aaa publicMethod = Aaa.INSTANCE;

    @Inject
    private void setPrivateMethod(Aaa privateMethod) {
        this.privateMethod = privateMethod;
    }

    @Inject
    void setPackagePrivateMethod(Aaa packagePrivateMethod) {
        this.packagePrivateMethod = packagePrivateMethod;
    }

    @Inject
    protected void setProtectedMethod(Aaa protectedMethod) {
        this.protectedMethod = protectedMethod;
    }

    @Inject
    public void setPublicMethod(Aaa publicMethod) {
        this.publicMethod = publicMethod;
    }

    // getter
    public Aaa getPrivateField() {
        return privateField;
    }

    public Aaa getPackagePrivateField() {
        return packagePrivateField;
    }

    public Aaa getProtectedField() {
        return protectedField;
    }

    public Aaa getPublicField() {
        return publicField;
    }

    public Aaa getPrivateMethod() {
        return privateMethod;
    }

    public Aaa getPackagePrivateMethod() {
        return packagePrivateMethod;
    }

    public Aaa getProtectedMethod() {
        return protectedMethod;
    }

    public Aaa getPublicMethod() {
        return publicMethod;
    }
}
