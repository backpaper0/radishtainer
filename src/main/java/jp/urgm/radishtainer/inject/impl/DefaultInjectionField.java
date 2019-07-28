package jp.urgm.radishtainer.inject.impl;

import java.lang.reflect.Field;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.DependencyResolver;
import jp.urgm.radishtainer.inject.InjectionMember;

public class DefaultInjectionField implements InjectionMember {

    private final Field field;
    private final DependencyResolver dependencyResolver;

    public DefaultInjectionField(final Field field, final DependencyResolver dependencyResolver) {
        this.field = field;
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public Object inject(final Container container, final Object component) {

        final Object dependency = dependencyResolver.resolve(container);

        if (field.isAccessible() == false) {
            field.setAccessible(true);
        }

        try {
            field.set(component, dependency);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
