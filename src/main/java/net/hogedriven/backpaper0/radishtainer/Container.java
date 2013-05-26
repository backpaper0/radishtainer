package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Provider;
import javax.inject.Singleton;
import net.hogedriven.backpaper0.radishtainer.event.Observes;

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
        return (T) getInstance(descriptor);
    }

    private Object getInstance(Descriptor<?> descriptor) {
        Object instance = instances.get(descriptor);
        if (instance != null) {
            inject(instance);
        } else {
            Class<?> impl = descriptors.get(descriptor);
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
        List<List<Field>> allFields = new ArrayList<>();
        List<List<Method>> allMethods = new ArrayList<>();
        for (Class<?> clazz : classes) {
            allFields.add(Arrays.asList(clazz.getDeclaredFields()));
            List<Method> methods = new ArrayList<>();
            for (Method method : clazz.getDeclaredMethods()) {
                removeOverrided(allMethods, method);
                methods.add(method);
            }
            allMethods.add(methods);
        }
        for (int i = 0; i < allFields.size(); i++) {
            for (Field field : allFields.get(i)) {
                Injector injector = new FieldInjector(field);
                if (injector.isInjectable()) {
                    injector.inject(this, target);
                }
            }
            for (Method method : allMethods.get(i)) {
                Injector injector = new MethodInjector(method);
                if (injector.isInjectable()) {
                    injector.inject(this, target);
                }
            }
        }
    }

    private void removeOverrided(List<List<Method>> allMethods, Method method) {
        for (List<Method> methods : allMethods) {
            for (Method other : methods) {
                if (isOverrideForm(method, other)) {
                    methods.remove(other);
                    return;
                }
            }
        }
    }

    static boolean isOverrideForm(Method method, Method other) {
        if (method.getDeclaringClass() == other.getDeclaringClass()) {
            return false;
        }
        if (other.getDeclaringClass().isAssignableFrom(method.getDeclaringClass()) == false) {
            return false;
        }
        if (Objects.equals(method.getName(), other.getName()) == false) {
            return false;
        }
        if (Arrays.equals(method.getParameterTypes(), other.getParameterTypes()) == false) {
            return false;
        }
        if (Modifier.isPrivate(other.getModifiers())) {
            return false;
        }
        if (Modifier.isProtected(other.getModifiers()) == false
                && Modifier.isPublic(other.getModifiers()) == false
                && Objects.equals(method.getDeclaringClass().getPackage(),
                other.getDeclaringClass().getPackage()) == false) {
            return false;
        }
        return true;
    }

    public void fireEvent(Object event) {
        for (Map.Entry<Descriptor<?>, Class<?>> entry : descriptors.entrySet()) {
            Class<?> impl = entry.getValue();
            for (Method method : impl.getDeclaredMethods()) {
                if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == event.getClass()) {
                    boolean b = false;
                    Annotation[] annotations = method.getParameterAnnotations()[0];
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() == Observes.class) {
                            b = true;
                        }
                    }
                    if (b) {
                        Descriptor<?> descriptor = entry.getKey();
                        Object instance = getInstance(descriptor);
                        if (method.isAccessible() == false) {
                            method.setAccessible(true);
                        }
                        try {
                            method.invoke(instance, event);
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
}
