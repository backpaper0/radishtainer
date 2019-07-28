package jp.urgm.radishtainer;

import javax.inject.Provider;

public interface Scope {

    Object getComponent(Class<?> clazz, Provider<?> provider);
}
