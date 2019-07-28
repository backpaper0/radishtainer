package jp.urgm.radishtainer.container;

import java.util.Map;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.component.Definition;
import jp.urgm.radishtainer.component.Key;

public class DefaultContainer implements Container {

    private final Map<Key, Definition> definitions;

    public DefaultContainer(final Map<Key, Definition> definitions) {
        this.definitions = definitions;
    }

    @Override
    public <T> T getComponent(final Class<T> clazz) {
        final Key key = new Key(clazz);
        final Definition definition = definitions.get(key);
        final Object component = definition.getComponent(this);
        return clazz.cast(component);
    }
}
