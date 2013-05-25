package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Provider;
import javax.inject.Singleton;

public class Container {

    private Map<Descriptor<?>, Class<?>> descriptors = new HashMap<>();

    private Map<Descriptor<?>, Object> singletonCache = new HashMap<>();

    public <T> void add(Class<T> type, Annotation qualifier, Class<? extends T> impl) {
        Descriptor<T> descriptor = new Descriptor<>(type, qualifier);
        descriptors.put(descriptor, impl != null ? impl : type);
    }

    public <T> T getInstance(Class<T> type, Annotation qualifier) {
        for (Descriptor<?> descriptor : descriptors.keySet()) {
            if (descriptor.match(type, qualifier)) {
                Class<?> impl = descriptors.get(descriptor);
                boolean isSingleton = false;
                for (Annotation annotation : impl.getAnnotations()) {
                    if (annotation.annotationType() == Singleton.class) {
                        isSingleton = true;
                        break;
                    }
                }
                Object instance = null;
                if (isSingleton) {
                    instance = singletonCache.get(descriptor);
                }
                if (instance == null) {
                    instance = newInstance(descriptor);
                    inject(instance);
                    if (isSingleton) {
                        singletonCache.put(descriptor, instance);
                    }
                }
                return (T) instance;
            }
        }
        throw new RuntimeException();
    }

    public <T> Provider<T> getProvider(final Class<T> type, final Annotation qualifier) {
        return new Provider<T>() {
            @Override
            public T get() {
                return getInstance(type, qualifier);
            }
        };
    }

    private Object newInstance(Descriptor<?> descriptor) {
        Class<?> clazz = descriptors.get(descriptor);
        List<Injector> injectors = new ArrayList<>();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            Injector injector = new ConstructorInjector(constructor);
            if (injector.isInjectable()) {
                injectors.add(injector);
            }
        }
        if (injectors.isEmpty()) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                Injector injector = new ConstructorInjector(constructor);
                injectors.add(injector);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return injectors.get(0).inject(this, null);
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
