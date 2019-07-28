package jp.urgm.radishtainer.container;

import java.util.Map;
import java.util.Set;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.component.Definition;
import jp.urgm.radishtainer.component.Key;

public class DefaultContainer implements Container {

    private final Map<Key, Definition> definitions;
    private final Map<Key, Set<Key>> aliases;

    public DefaultContainer(final Map<Key, Definition> definitions,
            final Map<Key, Set<Key>> aliases) {
        this.definitions = definitions;
        this.aliases = aliases;
    }

    @Override
    public <T> T getComponent(final Class<T> clazz) {
        final Key key = new Key(clazz);
        Definition definition = definitions.get(key);
        if (definition == null) {
            final Set<Key> keys = aliases.get(key);
            definition = definitions.get(keys.iterator().next());
        }
        final Object component = definition.getComponent(this);
        return clazz.cast(component);
    }
}
