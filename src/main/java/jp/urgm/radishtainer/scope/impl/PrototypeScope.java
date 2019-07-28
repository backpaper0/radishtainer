package jp.urgm.radishtainer.scope.impl;

import javax.inject.Provider;

import jp.urgm.radishtainer.scope.Scope;

public class PrototypeScope implements Scope {

    @Override
    public Object getComponent(final Class<?> clazz, final Provider<?> provider) {
        return provider.get();
    }
}
