package jp.urgm.radishtainer.annotation.component.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.urgm.radishtainer.annotation.inject.factory.AnnotationInjectionConstructorFactory;
import jp.urgm.radishtainer.annotation.inject.factory.AnnotationInjectionMemberFactory;
import jp.urgm.radishtainer.annotation.scope.AnnotationScopeResolver;
import jp.urgm.radishtainer.component.Definition;
import jp.urgm.radishtainer.component.factory.DefinitionFactory;
import jp.urgm.radishtainer.inject.InjectionConstructor;
import jp.urgm.radishtainer.inject.InjectionMember;
import jp.urgm.radishtainer.inject.factory.InjectionConstructorFactory;
import jp.urgm.radishtainer.inject.factory.InjectionMemberFactory;
import jp.urgm.radishtainer.reflect.MethodIterable;
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
        final List<Map.Entry<Integer, InjectionMember>> orderAndMembers = new ArrayList<>();

        final Map<Class<?>, Integer> orders = new HashMap<>();
        int order = 0;
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            orders.put(c, order++);
        }

        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            for (final Field field : c.getDeclaredFields()) {
                final Class<?> dc = c;
                injectionMemberFactory.fromField(field).ifPresent(a -> {
                    final Map.Entry<Integer, InjectionMember> entry = new AbstractMap.SimpleEntry<>(
                            orders.get(dc), a);
                    orderAndMembers.add(entry);
                });
            }
        }

        for (final Method method : new MethodIterable(clazz)) {
            final Class<?> dc = method.getDeclaringClass();
            injectionMemberFactory.fromMethod(method).ifPresent(a -> {
                final Map.Entry<Integer, InjectionMember> entry = new AbstractMap.SimpleEntry<>(
                        orders.get(dc), a);
                orderAndMembers.add(entry);
            });
        }

        final Comparator<? super Map.Entry<Integer, InjectionMember>> comparator = Comparator
                .comparing(Map.Entry::getKey);
        final List<InjectionMember> members = orderAndMembers.stream().sorted(comparator.reversed())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        final Scope scope = scopeResolver.resolve(clazz);
        return new Definition(clazz, constructor, members, scope);
    }
}
