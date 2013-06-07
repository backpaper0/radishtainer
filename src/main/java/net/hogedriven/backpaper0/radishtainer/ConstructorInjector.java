package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class ConstructorInjector extends Injector {

    private final Constructor<?> constructor;

    public ConstructorInjector(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public Object inject(Container container, Object target) {
        Class<?>[] types = constructor.getParameterTypes();
        Type[] genericTypes = constructor.getGenericParameterTypes();
        Annotation[][] annotations = constructor.getParameterAnnotations();
        Object[] dependencies = getDependencies(container, types, genericTypes, annotations, 0);
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
