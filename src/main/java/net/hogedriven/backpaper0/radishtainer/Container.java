package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.inject.Provider;
import javax.inject.Singleton;

public class Container {

    private Map<Descriptor, Binding> bindings = new HashMap<>();

    private Scope defaultScope = new Scope() {
        @Override
        public Object getInstance(Container container, Class<?> impl) {
            Object instance = container.newInstance(impl);
            container.inject(instance);
            return instance;
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
        Descriptor descriptor = new Descriptor(type, qualifier);
        if (bindings.containsKey(descriptor)) {
            throw new RuntimeException();
        }
        impl = impl != null ? impl : type;
        check(impl);
        Scope scope = findScope(impl);
        bindings.put(descriptor, Binding.newClassBinding(impl, scope));
    }

    public <T> void addInstance(Class<T> type, Annotation qualifier, T instance) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        if (instance == null) {
            throw new IllegalArgumentException("instance");
        }
        Descriptor descriptor = new Descriptor(type, qualifier);
        if (bindings.containsKey(descriptor)) {
            throw new RuntimeException();
        }
        bindings.put(descriptor, Binding.newInstanceBinding(instance));
    }

    public <T> T getInstance(Class<T> type, Annotation qualifier) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        Descriptor descriptor = new Descriptor(type, qualifier);
        check(descriptor);
        return (T) getInstance(descriptor);
    }

    private void check(Descriptor descriptor) {
        if (bindings.containsKey(descriptor) == false && bindings.containsKey(descriptor) == false) {
            throw new NoSuchElementException();
        }
    }

    private void check(Class<?> impl) {
        if (impl.isInterface()) {
            throw new IllegalArgumentException();
        }
        if (impl.isEnum()) {
            throw new IllegalArgumentException();
        }
        ClassInfo classInfo = new ClassInfo(impl);
        List<Constructor<?>> injectableConstructors = classInfo.getInjectableConstructors();
        if (injectableConstructors.isEmpty()) {
            try {
                classInfo.getDefaultConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
            return;
        }
        if (injectableConstructors.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (injectableConstructors.size() > 1) {
            throw new IllegalArgumentException();
        }
    }

    private Object getInstance(Descriptor descriptor) {
        Binding binding = bindings.get(descriptor);
        Object instance = binding.getInstance(this);
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

    public <T> Provider<T> getProvider(Class<T> type, Annotation qualifier) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        final Descriptor descriptor = new Descriptor(type, qualifier);
        check(descriptor);
        return new Provider<T>() {
            @Override
            public T get() {
                return (T) getInstance(descriptor);
            }
        };
    }

    public Object newInstance(Class<?> clazz) {
        ClassInfo classInfo = new ClassInfo(clazz);
        List<Constructor<?>> constructors = classInfo.getInjectableConstructors();
        Constructor<?> constructor;
        if (constructors.isEmpty() == false) {
            constructor = constructors.get(0);
        } else {
            try {
                constructor = classInfo.getDefaultConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return new ConstructorInjector(constructor).inject(this, null);
    }

    public void inject(Object target) {
        ClassInfo injectable = new ClassInfo(target.getClass());
        for (int i = 0; i < injectable.getInjectableFields().size(); i++) {
            for (Field field : injectable.getInjectableFields().get(i)) {
                Injector injector = new FieldInjector(field);
                injector.inject(this, target);
            }
            for (Method method : injectable.getInjectableMethods().get(i)) {
                Injector injector = new MethodInjector(method);
                injector.inject(this, target);
            }
        }
    }

    public void fireEvent(Object event) {
        for (Map.Entry<Descriptor, Binding> entry : bindings.entrySet()) {
            Descriptor descriptor = entry.getKey();
            Binding binding = entry.getValue();
            fireEvent(binding, event, descriptor);
        }
    }

    private void fireEvent(Binding binding, Object event, Descriptor descriptor) {
        ClassInfo handleable = binding.getClassInfo();
        for (List<Method> methods : handleable.getObservableMethods(event.getClass())) {
            for (Method method : methods) {
                Object instance = getInstance(descriptor);
                Injector injector = new EventInjector(method, event);
                injector.inject(this, instance);
            }
        }
    }
}
