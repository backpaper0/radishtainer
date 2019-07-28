package jp.urgm.radishtainer.scope;

import javax.inject.Provider;

public interface Scope {

    Object getComponent(Class<?> clazz, Provider<?> provider);
}
