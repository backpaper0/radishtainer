package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.inject.Provider;
import javax.inject.Singleton;
import net.hogedriven.backpaper0.radishtainer.event.Observes;

public class Container {

    private Map<Descriptor<?>, Class<?>> descriptors = new HashMap<>();

    private Map<Descriptor<?>, Object> instances = new HashMap<>();

    private Map<Class<?>, ClassInfo> classInfoCache = new HashMap<>();

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
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType");
        }
        if (scope == null) {
            throw new IllegalArgumentException("scope");
        }
        if (scopes.containsKey(annotationType)) {
            throw new RuntimeException();
        }
        scopes.put(annotationType, scope);
        Class<Scope> type = (Class<Scope>) scope.getClass();
        addInstance(type, null, scope);
    }

    public <T> void add(Class<T> type, Annotation qualifier, Class<? extends T> impl) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        Descriptor<T> descriptor = new Descriptor<>(type, qualifier);
        if (descriptors.containsKey(descriptor)) {
            throw new RuntimeException();
        }
        impl = impl != null ? impl : type;
        if (impl.isInterface()) {
            throw new IllegalArgumentException();
        }
        if (impl.isEnum()) {
            throw new IllegalArgumentException();
        }
        descriptors.put(descriptor, impl);
    }

    public <T> void addInstance(Class<T> type, Annotation qualifier, T instance) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        if (instance == null) {
            throw new IllegalArgumentException("instance");
        }
        Descriptor<T> descriptor = new Descriptor<>(type, qualifier);
        if (instances.containsKey(descriptor)) {
            throw new RuntimeException();
        }
        instances.put(descriptor, instance);
    }

    public <T> T getInstance(Class<T> type, Annotation qualifier) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        Descriptor<?> descriptor = new Descriptor<>(type, qualifier);
        return (T) getInstance(descriptor);
    }

    private Object getInstance(Descriptor<?> descriptor) {
        Object instance = instances.get(descriptor);
        if (instance != null) {
            inject(instance);
        } else {
            Class<?> impl = descriptors.get(descriptor);
            if (impl == null) {
                throw new NoSuchElementException();
            }
            Scope scope = findScope(impl);
            Instantiator instantiator = new Instantiator(this, impl);
            instance = scope.getInstance(instantiator, impl);
        }
        return instance;
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
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        Descriptor<?> descriptor = new Descriptor<>(type, qualifier);
        if (instances.containsKey(descriptor) == false && descriptors.containsKey(descriptor) == false) {
            throw new NoSuchElementException();
        }
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
        ClassInfo injectable = getClassInfo(target.getClass());
        for (int i = 0; i < injectable.allFields.size(); i++) {
            for (Field field : injectable.allFields.get(i)) {
                Injector injector = new FieldInjector(field);
                if (injector.isInjectable()) {
                    injector.inject(this, target);
                }
            }
            for (Method method : injectable.allMethods.get(i)) {
                Injector injector = new MethodInjector(method);
                if (injector.isInjectable()) {
                    injector.inject(this, target);
                }
            }
        }
    }

    public void fireEvent(Object event) {
        for (Map.Entry<Descriptor<?>, Class<?>> entry : descriptors.entrySet()) {
            Descriptor<?> descriptor = entry.getKey();
            Class<?> impl = entry.getValue();
            fireEvent(impl, event, descriptor);
        }
        for (Map.Entry<Descriptor<?>, Object> entry : instances.entrySet()) {
            Descriptor<?> descriptor = entry.getKey();
            Class<?> impl = entry.getValue().getClass();
            fireEvent(impl, event, descriptor);
        }
    }

    private void fireEvent(Class<?> impl, Object event, Descriptor<?> descriptor) {
        ClassInfo handleable = getClassInfo(impl);
        for (List<Method> methods : handleable.allMethods) {
            for (Method method : methods) {
                Class<?>[] types = method.getParameterTypes();
                if (types.length > 0 && method.getParameterTypes()[0] == event.getClass()) {
                    boolean b = false;
                    for (Annotation annotation : method.getParameterAnnotations()[0]) {
                        if (annotation.annotationType() == Observes.class) {
                            b = true;
                        }
                    }
                    if (b) {
                        Object instance = getInstance(descriptor);
                        Injector injector = new EventInjector(method, event);
                        injector.inject(this, instance);
                    }
                }
            }
        }
    }

    private ClassInfo getClassInfo(Class<?> c) {
        ClassInfo classInfo = classInfoCache.get(c);
        if (classInfo == null) {
            classInfo = new ClassInfo(c);
            classInfoCache.put(c, classInfo);
        }
        return classInfo;
    }
}
