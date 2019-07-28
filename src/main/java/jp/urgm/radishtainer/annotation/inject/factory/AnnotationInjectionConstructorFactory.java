package jp.urgm.radishtainer.annotation.inject.factory;

import java.lang.reflect.Constructor;
import java.util.Optional;

import javax.inject.Inject;

import jp.urgm.radishtainer.inject.InjectionConstructor;
import jp.urgm.radishtainer.inject.factory.InjectionConstructorFactory;
import jp.urgm.radishtainer.inject.impl.DefaultInjectionConstructor;

public class AnnotationInjectionConstructorFactory implements InjectionConstructorFactory {

    @Override
    public Optional<InjectionConstructor> create(final Class<?> clazz) {

        Constructor<?> defaultConstructor = null;

        for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                final InjectionConstructor injectionConstructor = new DefaultInjectionConstructor(
                        constructor);
                return Optional.of(injectionConstructor);
            }
            if (constructor.getParameterCount() == 0) {
                defaultConstructor = constructor;
            }
        }

        if (defaultConstructor != null) {
            final Constructor<?> constructor = defaultConstructor;
            final InjectionConstructor injectionConstructor = new DefaultInjectionConstructor(
                    constructor);
            return Optional.of(injectionConstructor);
        }

        return Optional.empty();
    }
}
