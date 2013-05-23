package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import javax.inject.Inject;
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
            dependencies[i] = container.getInstance(type, qualifier);
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
