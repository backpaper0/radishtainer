package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.inject.Inject;

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
        Object dependency = container.getInstance(type, null);
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
