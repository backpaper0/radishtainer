package jp.urgm.radishtainer.scope.impl;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Provider;

import jp.urgm.radishtainer.scope.Scope;

public class SingletonScope implements Scope {

    private final Map<Class<?>, Object> components = new HashMap<>();

    @Override
    public Object getComponent(final Class<?> clazz, final Provider<?> provider) {
        if (components.containsKey(clazz) == false) {
            final Object component = provider.get();
            components.put(clazz, component);
        }
        return components.get(clazz);
    }
}
