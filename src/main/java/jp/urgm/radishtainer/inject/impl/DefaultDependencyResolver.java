package jp.urgm.radishtainer.inject.impl;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.DependencyResolver;

public class DefaultDependencyResolver implements DependencyResolver {

    private final Class<?> clazz;

    public DefaultDependencyResolver(final Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object resolve(final Container container) {
        return container.getComponent(clazz);
    }
}
