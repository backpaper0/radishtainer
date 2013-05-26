package net.hogedriven.backpaper0.radishtainer.test;

import java.util.HashMap;
import java.util.Map;
import net.hogedriven.backpaper0.radishtainer.Instantiator;
import net.hogedriven.backpaper0.radishtainer.Scope;
import net.hogedriven.backpaper0.radishtainer.event.Observes;

public class Qqq1 implements Scope {

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

    public void init(@Observes Aaa aaa) {
        instances = new HashMap<>();
    }
}
