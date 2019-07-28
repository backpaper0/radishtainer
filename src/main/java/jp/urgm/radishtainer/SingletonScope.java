package jp.urgm.radishtainer;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

public class SingletonScope implements Scope {

    private final Map<Class<?>, Object> components = new HashMap<>();

    @Override
    public Object getComponent(final Class<?> clazz, final Provider<?> provider) {
        if (components.containsKey(clazz) == false) {
            final Object component = provider.get();
            components.put(clazz, component);
        }
        return components.get(clazz);
    }
}
