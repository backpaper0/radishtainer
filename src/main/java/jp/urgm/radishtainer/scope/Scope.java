package jp.urgm.radishtainer.scope;

import jakarta.inject.Provider;

public interface Scope {

    Object getComponent(Class<?> clazz, Provider<?> provider);
}
