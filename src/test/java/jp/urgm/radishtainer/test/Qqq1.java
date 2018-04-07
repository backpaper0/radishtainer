package jp.urgm.radishtainer.test;

import java.util.HashMap;
import java.util.Map;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.Scope;
import jp.urgm.radishtainer.event.Observes;

public class Qqq1 implements Scope {

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

    public void init(@Observes Aaa aaa) {
        instances = new HashMap<>();
    }
}
