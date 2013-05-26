package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.inject.Provider;
import javax.inject.Qualifier;

public abstract class Injector {

    public abstract boolean isInjectable();

    public abstract Object inject(Container container, Object target);

    protected Object getDependency(Container container, Class<?> type, Type genericType, Annotation[] annotations) {
        Annotation qualifier = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                qualifier = annotation;
            }
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

    protected Object[] getDependencies(Container container, Class<?>[] types, Type[] genericTypes, Annotation[][] annotations, int startIndex) {
        Object[] dependencies = new Object[types.length];
        for (int i = startIndex; i < types.length; i++) {
            dependencies[i] = getDependency(container, types[i], genericTypes[i], annotations[i]);
        }
        return dependencies;
    }
}
