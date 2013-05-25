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

    private Map<Descriptor<?>, Object> instances = new HashMap<>();

    private Scope defaultScope = new Scope() {
        @Override
        public Object getInstance(Instantiator instantiator, Class<?> impl) {
            return instantiator.newInstance();
        }
    };

    private Map<Class<? extends Annotation>, Scope> scopes = new HashMap<>();

    public Container() {
        addScope(Singleton.class, new SingletonScope());
    }

    public void addScope(Class<? extends Annotation> annotationType, Scope scope) {
        scopes.put(annotationType, scope);
    }

    public <T> void add(Class<T> type, Annotation qualifier, Class<? extends T> impl) {
        Descriptor<T> descriptor = new Descriptor<>(type, qualifier);
        descriptors.put(descriptor, impl != null ? impl : type);
    }

    public <T> void addInstance(Class<T> type, Annotation qualifier, T instance) {
        Descriptor<T> descriptor = new Descriptor<>(type, qualifier);
        instances.put(descriptor, instance);
    }

    public <T> T getInstance(Class<T> type, Annotation qualifier) {
        Descriptor<?> descriptor = new Descriptor<>(type, qualifier);
        Object instance = instances.get(descriptor);
        if (instance != null) {
            inject(instance);
        } else {
            Class<?> impl = descriptors.get(descriptor);
            Scope scope = findScope(impl);
            Instantiator instantiator = new Instantiator(this, impl);
            instance = scope.getInstance(instantiator, impl);
        }
        return (T) instance;
    }

    private Scope findScope(Class<?> impl) {
        for (Annotation annotation : impl.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.isAnnotationPresent(javax.inject.Scope.class)) {
                Scope scope = scopes.get(annotationType);
                return scope;
            }
        }
        return defaultScope;
    }

    public <T> Provider<T> getProvider(final Class<T> type, final Annotation qualifier) {
        return new Provider<T>() {
            @Override
            public T get() {
                return getInstance(type, qualifier);
            }
        };
    }

    Object newInstance(Class<?> clazz) {
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
