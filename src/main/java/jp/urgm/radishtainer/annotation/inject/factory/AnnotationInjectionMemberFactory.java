package jp.urgm.radishtainer.annotation.inject.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import jp.urgm.radishtainer.inject.DependencyResolver;
import jp.urgm.radishtainer.inject.InjectionMember;
import jp.urgm.radishtainer.inject.factory.DependencyResolverFactory;
import jp.urgm.radishtainer.inject.factory.InjectionMemberFactory;
import jp.urgm.radishtainer.inject.impl.DefaultInjectionField;
import jp.urgm.radishtainer.inject.impl.DefaultInjectionMethod;

public class AnnotationInjectionMemberFactory implements InjectionMemberFactory {

    private final DependencyResolverFactory dependencyResolverFactory = new AnnotationDependencyResolverFactory();

    @Override
    public Optional<InjectionMember> fromMethod(final Method method) {
        if (method.isAnnotationPresent(Inject.class) == false) {
            return Optional.empty();
        }
        final List<DependencyResolver> dependencyResolvers = new ArrayList<>();
        for (int i = 0; i < method.getParameterCount(); i++) {
            final DependencyResolver dependencyResolver = dependencyResolverFactory.fromParameter(
                    method,
                    i);
            dependencyResolvers.add(dependencyResolver);
        }
        final InjectionMember injectionMember = new DefaultInjectionMethod(method,
                dependencyResolvers);
        return Optional.of(injectionMember);
    }

    @Override
    public Optional<InjectionMember> fromField(final Field field) {
        if (field.isAnnotationPresent(Inject.class) == false) {
            return Optional.empty();
        }
        final DependencyResolver dependencyResolver = dependencyResolverFactory.fromField(field);
        final InjectionMember injectionMember = new DefaultInjectionField(field,
                dependencyResolver);
        return Optional.of(injectionMember);
    }
}
