package jp.urgm.radishtainer.inject.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.DependencyResolver;
import jp.urgm.radishtainer.inject.InjectionConstructor;

public class DefaultInjectionConstructor implements InjectionConstructor {

    private final Constructor<?> constructor;
    private final List<DependencyResolver> dependencyResolvers;

    public DefaultInjectionConstructor(final Constructor<?> constructor,
            final List<DependencyResolver> dependencyResolvers) {
        this.constructor = constructor;
        this.dependencyResolvers = dependencyResolvers;
    }

    @Override
    public Object inject(final Container container) {

        final Object[] dependencies = dependencyResolvers.stream().map(a -> a.resolve(container))
                .toArray();

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
