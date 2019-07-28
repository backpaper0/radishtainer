package jp.urgm.radishtainer.annotation.inject.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import jp.urgm.radishtainer.inject.DependencyResolver;
import jp.urgm.radishtainer.inject.InjectionConstructor;
import jp.urgm.radishtainer.inject.factory.DependencyResolverFactory;
import jp.urgm.radishtainer.inject.factory.InjectionConstructorFactory;
import jp.urgm.radishtainer.inject.impl.DefaultInjectionConstructor;

public class AnnotationInjectionConstructorFactory implements InjectionConstructorFactory {

    private final DependencyResolverFactory dependencyResolverFactory = new AnnotationDependencyResolverFactory();

    @Override
    public Optional<InjectionConstructor> create(final Class<?> clazz) {

        Constructor<?> defaultConstructor = null;

        for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                final List<DependencyResolver> dependencyResolvers = new ArrayList<>();
                for (int i = 0; i < constructor.getParameterCount(); i++) {
                    final DependencyResolver dependencyResolver = dependencyResolverFactory
                            .fromParameter(constructor, i);
                    dependencyResolvers.add(dependencyResolver);
                }
                final InjectionConstructor injectionConstructor = new DefaultInjectionConstructor(
                        constructor, dependencyResolvers);
                return Optional.of(injectionConstructor);
            }
            if (constructor.getParameterCount() == 0) {
                defaultConstructor = constructor;
            }
        }

        if (defaultConstructor != null) {
            final Constructor<?> constructor = defaultConstructor;
            final List<DependencyResolver> dependencyResolvers = Collections.emptyList();
            final InjectionConstructor injectionConstructor = new DefaultInjectionConstructor(
                    constructor, dependencyResolvers);
            return Optional.of(injectionConstructor);
        }

        return Optional.empty();
    }
}
