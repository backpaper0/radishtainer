package jp.urgm.radishtainer;

public interface Container {

    <T> T getComponent(Class<T> clazz);
}
