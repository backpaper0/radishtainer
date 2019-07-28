package jp.urgm.radishtainer.annotation.component.factory;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.inject.Qualifier;

import jp.urgm.radishtainer.component.Key;
import jp.urgm.radishtainer.component.factory.KeyFactory;

public class AnnotationKeyFactory implements KeyFactory {

    @Override
    public Key create(final Class<?> clazz) {
        final Annotation[] qualifiers = Arrays.stream(clazz.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .toArray(Annotation[]::new);
        return new Key(clazz, qualifiers);
    }
}
