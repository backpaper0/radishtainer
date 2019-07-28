package jp.urgm.radishtainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.inject.Provider;

public class Definition {

    private final Class<?> clazz;
    private final Scope scope;

    public Definition(final Class<?> clazz, final Scope scope) {
        this.clazz = clazz;
        this.scope = scope;
    }

    public Object getComponent(final Container container) {
        final Provider<?> provider = () -> {
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
        };
        return scope.getComponent(clazz, provider);
    }
}
