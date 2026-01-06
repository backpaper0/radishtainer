package jp.urgm.radishtainer.annotation.scope;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Singleton;

import jp.urgm.radishtainer.Prototype;
import jp.urgm.radishtainer.scope.Scope;
import jp.urgm.radishtainer.scope.ScopeResolver;
import jp.urgm.radishtainer.scope.impl.PrototypeScope;
import jp.urgm.radishtainer.scope.impl.SingletonScope;

public class AnnotationScopeResolver implements ScopeResolver {

    private final Scope defaultScope = new PrototypeScope();
    private final Map<Class<? extends Annotation>, Scope> scopes = new HashMap<>();

    public AnnotationScopeResolver() {
        scopes.put(Singleton.class, new SingletonScope());
        scopes.put(Prototype.class, defaultScope);
    }

    @Override
    public Scope resolve(final Class<?> clazz) {
        return Arrays.stream(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .filter(a -> a.isAnnotationPresent(jakarta.inject.Scope.class))
                .map(scopes::get)
                .findAny()
                .orElse(defaultScope);
    }
}
