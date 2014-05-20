package net.hogedriven.backpaper0.radishtainer;

@FunctionalInterface
public interface Scope {

    Object getInstance(Container container, Class<?> impl);
}
