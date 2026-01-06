package jp.urgm.radishtainer.component;

import java.util.List;

import jakarta.inject.Provider;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.InjectionConstructor;
import jp.urgm.radishtainer.inject.InjectionMember;
import jp.urgm.radishtainer.scope.Scope;

public class Definition {

    private final Class<?> clazz;
    private final InjectionConstructor constructor;
    private final List<InjectionMember> members;
    private final Scope scope;

    public Definition(final Class<?> clazz, final InjectionConstructor constructor,
            final List<InjectionMember> members, final Scope scope) {
        this.clazz = clazz;
        this.constructor = constructor;
        this.members = members;
        this.scope = scope;
    }

    public Object getComponent(final Container container) {
        final Provider<?> provider = () -> {
            final Object component = constructor.inject(container);
            members.forEach(m -> m.inject(container, component));
            return component;
        };
        return scope.getComponent(clazz, provider);
    }
}
