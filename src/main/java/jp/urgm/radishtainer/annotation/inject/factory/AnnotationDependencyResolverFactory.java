package jp.urgm.radishtainer.annotation.inject.factory;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;

import jp.urgm.radishtainer.inject.DependencyResolver;
import jp.urgm.radishtainer.inject.factory.DependencyResolverFactory;
import jp.urgm.radishtainer.inject.impl.DefaultDependencyResolver;

public class AnnotationDependencyResolverFactory implements DependencyResolverFactory {

    @Override
    public DependencyResolver fromParameter(final Executable executable, final int index) {
        final Class<?> clazz = executable.getParameterTypes()[index];
        return new DefaultDependencyResolver(clazz);
    }

    @Override
    public DependencyResolver fromField(final Field field) {
        final Class<?> clazz = field.getType();
        return new DefaultDependencyResolver(clazz);
    }
}
