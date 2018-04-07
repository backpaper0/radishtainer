package jp.urgm.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Provider;
import javax.inject.Qualifier;

public class Injector {

    private final Container container;

    public Injector(Container container) {
        this.container = container;
    }

    public Object inject(Field field, Object target) {
        Class<?> type = field.getType();
        Type genericType = field.getGenericType();
        Annotation[] annotations = field.getAnnotations();
        Object dependency = getDependency(type, genericType, annotations);
        if (Modifier.isPublic(field.getModifiers()) == false
                && field.isAccessible() == false) {
            field.setAccessible(true);
        }
        try {
            field.set(target, dependency);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Object inject(Method method, Object target) {
        Class<?>[] types = method.getParameterTypes();
        Type[] genericTypes = method.getGenericParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        Object[] dependencies = getDependencies(types, genericTypes, annotations, 0);
        if (Modifier.isPublic(method.getModifiers()) == false
                && method.isAccessible() == false) {
            method.setAccessible(true);
        }
        try {
            return method.invoke(target, dependencies);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public Object inject(Constructor<?> constructor) {
        Class<?>[] types = constructor.getParameterTypes();
        Type[] genericTypes = constructor.getGenericParameterTypes();
        Annotation[][] annotations = constructor.getParameterAnnotations();
        Object[] dependencies = getDependencies(types, genericTypes, annotations, 0);
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

    public Object inject(Method method, Object event, Object target) {
        Class<?>[] types = method.getParameterTypes();
        Type[] genericTypes = method.getGenericParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        Object[] dependencies = getDependencies(types, genericTypes, annotations, 1);
        dependencies[0] = event;
        if (Modifier.isPublic(method.getModifiers()) == false
                && method.isAccessible() == false) {
            method.setAccessible(true);
        }
        try {
            return method.invoke(target, dependencies);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    protected Object getDependency(Class<?> type, Type genericType, Annotation[] annotations) {
        List<Annotation> qualifiers = Arrays.stream(annotations)
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toList());
        Annotation qualifier = null;
        if (qualifiers.isEmpty() == false) {
            if (qualifiers.size() > 1) {
                throw new IllegalArgumentException();
            }
            qualifier = qualifiers.get(0);
        }
        Object dependency;
        if (type == Provider.class) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> type2 = (Class<?>) pt.getActualTypeArguments()[0];
            dependency = container.getProvider(type2, qualifier);
        } else {
            dependency = container.getInstance(type, qualifier);
        }
        return dependency;
    }

    protected Object[] getDependencies(Class<?>[] types, Type[] genericTypes, Annotation[][] annotations, int startIndex) {
        Object[] dependencies = new Object[types.length];
        for (int i = startIndex; i < types.length; i++) {
            dependencies[i] = getDependency(types[i], genericTypes[i], annotations[i]);
        }
        return dependencies;
    }
}
