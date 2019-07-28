package jp.urgm.radishtainer.inject.impl;

import java.lang.annotation.Annotation;

import javax.inject.Provider;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.DependencyResolver;

public class DefaultDependencyResolver implements DependencyResolver {

    private final Class<?> clazz;
    private final Annotation[] qualifiers;
    private final boolean provider;

    public DefaultDependencyResolver(final Class<?> clazz, final Annotation[] qualifiers,
            final boolean provider) {
        this.clazz = clazz;
        this.qualifiers = qualifiers;
        this.provider = provider;
    }

    @Override
    public Object resolve(final Container container) {
        if (provider) {
            return (Provider<?>) () -> container.getComponent(clazz, qualifiers);
        }
        return container.getComponent(clazz, qualifiers);
    }
}
