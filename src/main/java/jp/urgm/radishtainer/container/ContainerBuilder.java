package jp.urgm.radishtainer.container;

import java.util.HashMap;
import java.util.Map;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.annotation.component.factory.AnnotationDefinitionFactory;
import jp.urgm.radishtainer.component.Definition;
import jp.urgm.radishtainer.component.Key;
import jp.urgm.radishtainer.component.factory.DefinitionFactory;

public class ContainerBuilder {

    private final DefinitionFactory definitionFactory = new AnnotationDefinitionFactory();
    private final Map<Key, Definition> definitions = new HashMap<>();

    public ContainerBuilder register(final Class<?> clazz) {
        final Key key = new Key(clazz);
        final Definition definition = definitionFactory.create(clazz);
        definitions.put(key, definition);
        return this;
    }

    public Container build() {
        return new DefaultContainer(definitions);
    }
}
