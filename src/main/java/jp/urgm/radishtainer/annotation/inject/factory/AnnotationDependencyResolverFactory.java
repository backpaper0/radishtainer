package jp.urgm.radishtainer.annotation.inject.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.Arrays;

import javax.inject.Qualifier;

import jp.urgm.radishtainer.inject.DependencyResolver;
import jp.urgm.radishtainer.inject.factory.DependencyResolverFactory;
import jp.urgm.radishtainer.inject.impl.DefaultDependencyResolver;

public class AnnotationDependencyResolverFactory implements DependencyResolverFactory {

    @Override
    public DependencyResolver fromParameter(final Executable executable, final int index) {
        final Class<?> clazz = executable.getParameterTypes()[index];
        final Annotation[] qualifiers = Arrays.stream(executable.getParameterAnnotations()[index])
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .toArray(Annotation[]::new);
        return new DefaultDependencyResolver(clazz, qualifiers);
    }

    @Override
    public DependencyResolver fromField(final Field field) {
        final Class<?> clazz = field.getType();
        final Annotation[] qualifiers = Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .toArray(Annotation[]::new);
        return new DefaultDependencyResolver(clazz, qualifiers);
    }
}
