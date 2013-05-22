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
        List<Injector> injectors = new ArrayList<>();
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                Injector injector = new FieldInjector(field);
                injectors.remove(injector);
                if (injector.isInjectable()) {
                    injectors.add(injector);
                }
            }
            for (Method method : clazz.getDeclaredMethods()) {
                Injector injector = new MethodInjector(method);
                injectors.remove(injector);
                if (injector.isInjectable()) {
                    injectors.add(injector);
                }
            }
        }
        for (Injector injector : injectors) {
            injector.inject(this, target);
        }
    }
}
