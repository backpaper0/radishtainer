package jp.urgm.radishtainer.annotation.component.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.urgm.radishtainer.annotation.inject.factory.AnnotationInjectionConstructorFactory;
import jp.urgm.radishtainer.annotation.inject.factory.AnnotationInjectionMemberFactory;
import jp.urgm.radishtainer.annotation.scope.AnnotationScopeResolver;
import jp.urgm.radishtainer.component.Definition;
import jp.urgm.radishtainer.component.factory.DefinitionFactory;
import jp.urgm.radishtainer.inject.InjectionConstructor;
import jp.urgm.radishtainer.inject.InjectionMember;
import jp.urgm.radishtainer.inject.factory.InjectionConstructorFactory;
import jp.urgm.radishtainer.inject.factory.InjectionMemberFactory;
import jp.urgm.radishtainer.scope.Scope;
import jp.urgm.radishtainer.scope.ScopeResolver;

public class AnnotationDefinitionFactory implements DefinitionFactory {

    private final InjectionConstructorFactory injectionConstructorFactory = new AnnotationInjectionConstructorFactory();
    private final InjectionMemberFactory injectionMemberFactory = new AnnotationInjectionMemberFactory();
    private final ScopeResolver scopeResolver = new AnnotationScopeResolver();

    @Override
    public Definition create(final Class<?> clazz) {
        final InjectionConstructor constructor = injectionConstructorFactory.create(clazz)
                .orElseThrow(RuntimeException::new);
        final List<InjectionMember> members = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            for (final Field field : c.getDeclaredFields()) {
                injectionMemberFactory.fromField(field).ifPresent(members::add);
            }
        }

        final List<Method> methods = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            for (final Method method : c.getDeclaredMethods()) {
                if (isOverriden(methods, method) == false) {
                    methods.add(method);
                }
            }
        }
        for (final Method method : methods) {
            injectionMemberFactory.fromMethod(method).ifPresent(members::add);
        }

        final Scope scope = scopeResolver.resolve(clazz);
        return new Definition(clazz, constructor, members, scope);
    }

    private static boolean isOverriden(final List<Method> methods, final Method method) {
        for (final Method m : methods) {
            if (m.getName().equals(method.getName())
                    && Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
                return true;
            }
        }
        return false;
    }
}
