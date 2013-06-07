package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class EventInjector extends Injector {

    private final Method method;

    private final Object event;

    public EventInjector(Method method, Object event) {
        this.method = method;
        this.event = event;
    }

    @Override
    public Object inject(Container container, Object target) {
        Class<?>[] types = method.getParameterTypes();
        Type[] genericTypes = method.getGenericParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        Object[] dependencies = getDependencies(container, types, genericTypes, annotations, 1);
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
}
