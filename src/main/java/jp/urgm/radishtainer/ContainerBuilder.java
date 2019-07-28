package jp.urgm.radishtainer;

import java.util.HashMap;
import java.util.Map;

public class ContainerBuilder {

    private final Map<Key, Definition> definitions = new HashMap<>();

    public ContainerBuilder register(final Class<?> clazz) {
        final Key key = new Key(clazz);
        final Definition definition = new Definition(clazz);
        definitions.put(key, definition);
        return this;
    }

    public Container build() {
        return new DefaultContainer(definitions);
    }
}
