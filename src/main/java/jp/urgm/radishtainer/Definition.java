package jp.urgm.radishtainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Definition {

    private final Class<?> clazz;

    public Definition(final Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object getComponent(final Container container) {
        try {
            final Constructor<?> constructor = clazz.getDeclaredConstructor();
            if (constructor.isAccessible() == false) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | SecurityException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
