package net.hogedriven.backpaper0.radishtainer;

public class Instantiator {

    private final Container container;

    private final Class<?> impl;

    public Instantiator(Container container, Class<?> impl) {
        this.container = container;
        this.impl = impl;
    }

    public Object newInstance() {
        Object instance = container.newInstance(impl);
        container.inject(instance);
        return instance;
    }
}
