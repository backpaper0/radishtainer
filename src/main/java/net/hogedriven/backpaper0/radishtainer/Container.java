package net.hogedriven.backpaper0.radishtainer;

import java.util.HashSet;
import java.util.Set;

public class Container {

    private Set<Class<?>> types = new HashSet<>();

    public <T> void add(Class<T> type) {
        types.add(type);
    }

    public <T> T getInstance(Class<T> type) {
        for (Class<?> type2 : types) {
            if (type2 == type) {
                try {
                    return (T) type2.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException();
    }
}
