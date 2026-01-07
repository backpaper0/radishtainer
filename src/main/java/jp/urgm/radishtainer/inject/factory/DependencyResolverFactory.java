package jp.urgm.radishtainer.inject.factory;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;

import jp.urgm.radishtainer.inject.DependencyResolver;

public interface DependencyResolverFactory {

	DependencyResolver fromParameter(Executable executable, int index);

	DependencyResolver fromField(Field field);

}
