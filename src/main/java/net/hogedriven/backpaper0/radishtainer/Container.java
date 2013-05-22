package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

public class Container {

    private Set<Class<?>> types = new HashSet<>();

    public <T> void add(Class<T> type) {
        types.add(type);
    }

    public <T> T getInstance(Class<T> type) {
        for (Class<?> type2 : types) {
            if (type2 == type) {
                try {
                    return (T) type2.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException();
    }

    public void inject(Object target) {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> clazz = target.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            classes.add(clazz);
        }
        Collections.reverse(classes);
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Class<?> type = field.getType();
                    Object dependency = getInstance(type);
                    if (Modifier.isPublic(field.getModifiers()) == false
                            && field.isAccessible() == false) {
                        field.setAccessible(true);
                    }
                    try {
                        field.set(target, dependency);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Inject.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] dependencies = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        Class<?> type = parameterTypes[i];
                        dependencies[i] = getInstance(type);
                    }
                    if (Modifier.isPublic(method.getModifiers()) == false
                            && method.isAccessible() == false) {
                        method.setAccessible(true);
                    }
                    try {
                        method.invoke(target, dependencies);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e.getCause());
                    }
                }
            }
        }
    }
}
