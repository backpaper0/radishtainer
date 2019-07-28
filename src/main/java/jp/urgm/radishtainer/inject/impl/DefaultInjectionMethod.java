package jp.urgm.radishtainer.inject.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.InjectionMember;

public class DefaultInjectionMethod implements InjectionMember {

    private final Method method;

    public DefaultInjectionMethod(final Method method) {
        this.method = method;
    }

    @Override
    public Object inject(final Container container, final Object component) {

        final Object[] dependencies = new Object[method.getParameterCount()];
        for (int i = 0; i < dependencies.length; i++) {
            final Class<?> clazz = method.getParameterTypes()[i];
            dependencies[i] = container.getComponent(clazz);
        }

        if (method.isAccessible() == false) {
            method.setAccessible(true);
        }

        try {
            method.invoke(component, dependencies);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
