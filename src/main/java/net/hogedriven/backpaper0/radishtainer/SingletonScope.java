package net.hogedriven.backpaper0.radishtainer;

import java.util.HashMap;
import java.util.Map;

public class SingletonScope implements Scope {

    private Map<Class<?>, Object> instances = new HashMap<>();

    @Override
    public Object getInstance(Container container, Class<?> impl) {
        Object instance = instances.get(impl);
        if (instance == null) {
            instance = container.newInstance(impl);
            container.inject(instance);
            instances.put(impl, instance);
        }
        return instance;
    }
}
