package jp.urgm.radishtainer.annotation.scope;

import static org.junit.Assert.*;

import javax.inject.Singleton;

import org.junit.Test;

import jp.urgm.radishtainer.annotation.scope.AnnotationScopeResolver;
import jp.urgm.radishtainer.scope.Scope;
import jp.urgm.radishtainer.scope.impl.PrototypeScope;
import jp.urgm.radishtainer.scope.impl.SingletonScope;

public class AnnotationScopeResolverTest {

    @Test
    public void defaultScope() throws Exception {
        final AnnotationScopeResolver scopeResolver = new AnnotationScopeResolver();
        final Scope scope = scopeResolver.resolve(Aaa.class);
        assertEquals(PrototypeScope.class, scope.getClass());
    }

    @Test
    public void singletonScope() throws Exception {
        final AnnotationScopeResolver scopeResolver = new AnnotationScopeResolver();
        final Scope scope = scopeResolver.resolve(Bbb.class);
        assertEquals(SingletonScope.class, scope.getClass());
    }

    private static class Aaa {
    }

    @Singleton
    private static class Bbb {
    }
}
