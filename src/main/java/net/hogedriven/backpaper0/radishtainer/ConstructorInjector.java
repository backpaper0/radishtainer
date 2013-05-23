package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import javax.inject.Inject;

public class ConstructorInjector extends Injector {

    private final Constructor<?> constructor;

    public ConstructorInjector(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean isInjectable() {
        return  constructor.isAnnotationPresent(Inject.class);
    }

    @Override
    public Object inject(Container container, Object target) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            dependencies[i] = container.getInstance(type, null);
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
