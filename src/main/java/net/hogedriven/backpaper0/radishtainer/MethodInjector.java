package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;

public class MethodInjector extends Injector {

    private final Method method;

    public MethodInjector(Method method) {
        this.method = method;
    }

    @Override
    public boolean isInjectable() {
        return method.isAnnotationPresent(Inject.class);
    }

    @Override
    public Object inject(Container container, Object target) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            dependencies[i] = container.getInstance(type, null);
        }
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

    public boolean isOverrideForm(MethodInjector other) {
        if (method.getDeclaringClass() == other.method.getDeclaringClass()) {
            return false;
        }
        if (other.method.getDeclaringClass().isAssignableFrom(method.getDeclaringClass()) == false) {
            return false;
        }
        if (Objects.equals(method.getName(), other.method.getName()) == false) {
            return false;
        }
        if (Arrays.equals(method.getParameterTypes(), other.method.getParameterTypes()) == false) {
            return false;
        }
        if (Modifier.isPrivate(other.method.getModifiers())) {
            return false;
        }
        if (Modifier.isProtected(other.method.getModifiers()) == false
                && Modifier.isPublic(other.method.getModifiers()) == false
                && Objects.equals(method.getDeclaringClass().getPackage(),
                other.method.getDeclaringClass().getPackage()) == false) {
            return false;
        }
        return true;
    }
}
