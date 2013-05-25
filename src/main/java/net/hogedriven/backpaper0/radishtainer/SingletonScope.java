package net.hogedriven.backpaper0.radishtainer;

import java.util.HashMap;
import java.util.Map;

public class SingletonScope implements Scope {

    private Map<Class<?>, Object> instances = new HashMap<>();

    @Override
    public Object getInstance(Instantiator instantiator, Class<?> impl) {
        Object instance = instances.get(impl);
        if (instance == null) {
            instance = instantiator.newInstance();
            instances.put(impl, instance);
        }
        return instance;
    }
}
