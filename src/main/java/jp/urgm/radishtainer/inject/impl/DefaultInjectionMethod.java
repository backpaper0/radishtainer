package jp.urgm.radishtainer.inject.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.inject.DependencyResolver;
import jp.urgm.radishtainer.inject.InjectionMember;

public class DefaultInjectionMethod implements InjectionMember {

	private final Method method;

	private final List<DependencyResolver> dependencyResolvers;

	public DefaultInjectionMethod(final Method method, final List<DependencyResolver> dependencyResolvers) {
		this.method = method;
		this.dependencyResolvers = dependencyResolvers;
	}

	@Override
	public Object inject(final Container container, final Object component) {

		final Object[] dependencies = dependencyResolvers.stream().map(a -> a.resolve(container)).toArray();

		if (method.isAccessible() == false) {
			method.setAccessible(true);
		}

		try {
			method.invoke(component, dependencies);
		}
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		return null;
	}

}
