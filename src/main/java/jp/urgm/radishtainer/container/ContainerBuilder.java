package jp.urgm.radishtainer.container;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.annotation.component.factory.AnnotationDefinitionFactory;
import jp.urgm.radishtainer.annotation.component.factory.AnnotationKeyFactory;
import jp.urgm.radishtainer.component.Definition;
import jp.urgm.radishtainer.component.Key;
import jp.urgm.radishtainer.component.factory.DefinitionFactory;
import jp.urgm.radishtainer.component.factory.KeyFactory;

public class ContainerBuilder {

	private final KeyFactory keyFactory = new AnnotationKeyFactory();

	private final DefinitionFactory definitionFactory = new AnnotationDefinitionFactory();

	private final Map<Key, Definition> definitions = new HashMap<>();

	private final Map<Key, Set<Key>> aliases = new HashMap<>();

	public ContainerBuilder register(final Class<?> clazz) {
		final Key key = keyFactory.create(clazz);
		final Definition definition = definitionFactory.create(clazz);
		definitions.put(key, definition);

		for (final Key alias : key.getAliases()) {
			aliases.computeIfAbsent(alias, a -> new HashSet<>()).add(key);
		}

		return this;
	}

	public ContainerBuilder register(final Class<?> clazz, final Annotation... qualifiers) {
		final Key key = new Key(clazz, qualifiers);
		final Definition definition = definitionFactory.create(clazz);
		definitions.put(key, definition);

		for (final Key alias : key.getAliases()) {
			aliases.computeIfAbsent(alias, a -> new HashSet<>()).add(key);
		}

		return this;
	}

	public Container build() {
		return new DefaultContainer(keyFactory, definitions, aliases);
	}

}
