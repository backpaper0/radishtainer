package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

public class Container {

    private Set<Class<?>> types = new HashSet<>();

    public <T> void add(Class<T> type) {
        types.add(type);
    }

    public <T> T getInstance(Class<T> type) {
        for (Class<?> type2 : types) {
            if (type2 == type) {
                Object instance = newInstance(type);
                inject(instance);
                return (T) instance;
            }
        }
        throw new RuntimeException();
    }

    private Object newInstance(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> type = parameterTypes[i];
                    dependencies[i] = getInstance(type);
                }
                if (Modifier.isPublic(constructor.getModifiers()) == false
                        && constructor.isAccessible() == false) {
                    constructor.setAccessible(true);
                }
                try {
                    return constructor.newInstance(dependencies);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
                if (injector.isInjectable()) {
                    injectors.add(injector);
                }
            }
            for (Method method : clazz.getDeclaredMethods()) {
                MethodInjector injector = new MethodInjector(method);
                for (Injector other : injectors) {
                    if (other instanceof MethodInjector) {
                        if (injector.isOverrideForm((MethodInjector) other)) {
                            injectors.remove(other);
                            break;
                        }
                    }
                }
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
