package jp.urgm.radishtainer.test;

import javax.inject.Inject;
import javax.inject.Provider;

public class Kkk {

    public static final Provider<Aaa> PROVIDER = new Provider<Aaa>() {
        @Override
        public Aaa get() {
            throw new UnsupportedOperationException();
        }
    };

    //field injection
    @Inject
    private Provider<Aaa> field = PROVIDER;

    private Provider<Aaa> constructor = PROVIDER;

    private Provider<Aaa> method = PROVIDER;

    //constructor injection
    @Inject
    public Kkk(Provider<Aaa> constructor) {
        this.constructor = constructor;
    }

    //method injection
    @Inject
    public void setMethod(Provider<Aaa> method) {
        this.method = method;
    }

    //getter
    public Provider<Aaa> getField() {
        return field;
    }

    public Provider<Aaa> getConstructor() {
        return constructor;
    }

    public Provider<Aaa> getMethod() {
        return method;
    }
}
