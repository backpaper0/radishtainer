package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class MethodInjector extends Injector {

    private final Method method;

    private final Map<String, Object> metaData = new HashMap<>();

    public MethodInjector(Method method) {
        this.method = method;
        metaData.put("name", method.getName());
        int index = 0;
        for (Class<?> parameterType : method.getParameterTypes()) {
            metaData.put("parameterType" + index, parameterType);
            index++;
        }
    }

    @Override
    public boolean isInjectable() {
        return method.isAnnotationPresent(Inject.class);
    }

    @Override
    public void inject(Container container, Object target) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            dependencies[i] = container.getInstance(type);
        }
        if (Modifier.isPublic(method.getModifiers()) == false
                && method.isAccessible() == false) {
            method.setAccessible(true);
        }
        try {
            method.invoke(target, dependencies);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public int hashCode() {
        return metaData.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof MethodInjector) == false) {
            return false;
        }
        MethodInjector other = (MethodInjector) obj;
        return metaData.equals(other.metaData);
    }
}
