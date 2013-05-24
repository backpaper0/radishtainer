package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;

public class ConstructorInjector extends Injector {

    private final Constructor<?> constructor;

    public ConstructorInjector(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean isInjectable() {
        return constructor.isAnnotationPresent(Inject.class);
    }

    @Override
    public Object inject(Container container, Object target) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Type[] genericParameterTypes = constructor.getGenericParameterTypes();
        Annotation[][] annotations = constructor.getParameterAnnotations();
        Object[] dependencies = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            Annotation qualifier = null;
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                    qualifier = annotation;
                }
            }
            Object dependency;
            if (type == Provider.class) {
                ParameterizedType pt = (ParameterizedType) genericParameterTypes[i];
                Class<?> type2 = (Class<?>) pt.getActualTypeArguments()[0];
                dependency = container.getProvider(type2, qualifier);
            } else {
                dependency = container.getInstance(type, qualifier);
            }
            dependencies[i] = dependency;
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
