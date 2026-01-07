package jp.urgm.radishtainer.inject.factory;

import java.util.Optional;

import jp.urgm.radishtainer.inject.InjectionConstructor;

public interface InjectionConstructorFactory {

	Optional<InjectionConstructor> create(Class<?> clazz);

}
