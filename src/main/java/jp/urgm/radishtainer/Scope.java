package jp.urgm.radishtainer;

@FunctionalInterface
public interface Scope {

    Object getInstance(Container container, Class<?> impl);
}
