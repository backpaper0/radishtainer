package jp.urgm.radishtainer;

import java.lang.annotation.Annotation;

public interface Container {

    <T> T getComponent(Class<T> clazz);

    <T> T getComponent(Class<T> clazz, Annotation... qualifiers);
}
