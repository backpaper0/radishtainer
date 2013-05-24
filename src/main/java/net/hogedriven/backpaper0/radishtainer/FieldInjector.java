package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;

public class FieldInjector extends Injector {

    private final Field field;

    public FieldInjector(Field field) {
        this.field = field;
    }

    @Override
    public boolean isInjectable() {
        return field.isAnnotationPresent(Inject.class);
    }

    @Override
    public Object inject(Container container, Object target) {
        Class<?> type = field.getType();
        Type genericType = field.getGenericType();
        Annotation qualifier = null;
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                qualifier = annotation;
            }
        }
        Object dependency;
        if (type == Provider.class) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> type2 = (Class<?>) pt.getActualTypeArguments()[0];
            dependency = container.getProvider(type2, qualifier);
        } else {
            dependency = container.getInstance(type, qualifier);
        }
        if (Modifier.isPublic(field.getModifiers()) == false
                && field.isAccessible() == false) {
            field.setAccessible(true);
        }
        try {
            field.set(target, dependency);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
