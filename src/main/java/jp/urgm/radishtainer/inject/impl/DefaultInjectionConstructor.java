package jp.urgm.radishtainer.inject.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.InjectionConstructor;

public class DefaultInjectionConstructor implements InjectionConstructor {

    private final Constructor<?> constructor;

    public DefaultInjectionConstructor(final Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public Object inject(final Container container) {

        final Object[] dependencies = new Object[constructor.getParameterCount()];
        for (int i = 0; i < dependencies.length; i++) {
            final Class<?> clazz = constructor.getParameterTypes()[i];
            dependencies[i] = container.getComponent(clazz);
        }

        if (constructor.isAccessible() == false) {
            constructor.setAccessible(true);
        }

        try {
            return constructor.newInstance(dependencies);
        } catch (InstantiationException | IllegalAccessException | SecurityException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
