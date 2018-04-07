package jp.urgm.radishtainer.test;

import javax.inject.Inject;

public class Jjj {

    //field injection
    @Inject
    private Iii2 fieldPlain;

    @Iii1
    @Inject
    private Iii2 fieldWithQualifier;

    private final Iii2 constructorPlain;

    private final Iii2 constructorWithQualifier;

    private Iii2 methodPlain;

    private Iii2 methodWithQualifier;

    //constructor injection
    @Inject
    public Jjj(Iii2 constructorPlain, @Iii1 Iii2 constructorWithQualifier) {
        this.constructorPlain = constructorPlain;
        this.constructorWithQualifier = constructorWithQualifier;
    }

    //method injection
    @Inject
    public void setMethodPlain(Iii2 methodPlain) {
        this.methodPlain = methodPlain;
    }

    @Inject
    public void setMethodWithQualifier(@Iii1 Iii2 methodWithQualifier) {
        this.methodWithQualifier = methodWithQualifier;
    }

    //getter
    public Iii2 getFieldPlain() {
        return fieldPlain;
    }

    public Iii2 getFieldWithQualifier() {
        return fieldWithQualifier;
    }

    public Iii2 getConstructorPlain() {
        return constructorPlain;
    }

    public Iii2 getConstructorWithQualifier() {
        return constructorWithQualifier;
    }

    public Iii2 getMethodPlain() {
        return methodPlain;
    }

    public Iii2 getMethodWithQualifier() {
        return methodWithQualifier;
    }
}
