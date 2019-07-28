package jp.urgm.radishtainer.inject.impl;

import java.lang.annotation.Annotation;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.DependencyResolver;

public class DefaultDependencyResolver implements DependencyResolver {

    private final Class<?> clazz;
    private final Annotation[] qualifiers;

    public DefaultDependencyResolver(final Class<?> clazz, final Annotation... qualifiers) {
        this.clazz = clazz;
        this.qualifiers = qualifiers;
    }

    @Override
    public Object resolve(final Container container) {
        return container.getComponent(clazz, qualifiers);
    }
}
