package jp.urgm.radishtainer;

import javax.inject.Provider;

public class PrototypeScope implements Scope {

    @Override
    public Object getComponent(final Class<?> clazz, final Provider<?> provider) {
        return provider.get();
    }
}
