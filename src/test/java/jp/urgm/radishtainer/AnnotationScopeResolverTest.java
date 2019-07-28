package jp.urgm.radishtainer;

import static org.junit.Assert.*;

import javax.inject.Singleton;

import org.junit.Test;

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
