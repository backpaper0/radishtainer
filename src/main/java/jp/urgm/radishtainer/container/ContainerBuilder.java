package jp.urgm.radishtainer.container;

import java.util.HashMap;
import java.util.Map;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.annotation.scope.AnnotationScopeResolver;
import jp.urgm.radishtainer.component.Definition;
import jp.urgm.radishtainer.component.Key;
import jp.urgm.radishtainer.scope.Scope;
import jp.urgm.radishtainer.scope.ScopeResolver;

public class ContainerBuilder {

    private final ScopeResolver scopeResolver = new AnnotationScopeResolver();
    private final Map<Key, Definition> definitions = new HashMap<>();

    public ContainerBuilder register(final Class<?> clazz) {
        final Key key = new Key(clazz);

        final Scope scope = scopeResolver.resolve(clazz);

        final Definition definition = new Definition(clazz, scope);
        definitions.put(key, definition);
        return this;
    }

    public Container build() {
        return new DefaultContainer(definitions);
    }
}
