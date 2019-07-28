package jp.urgm.radishtainer.component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Key {

    private final Class<?> clazz;

    public Key(final Class<?> clazz) {
        this.clazz = clazz;
    }

    public Set<Key> getAliases() {
        final Set<Key> aliases = new HashSet<>();
        collect(clazz, aliases);
        return aliases;
    }

    private void collect(final Class<?> c, final Set<Key> aliases) {
        if (c != null && c != Object.class) {
            if (c != clazz) {
                aliases.add(new Key(c));
            }
            collect(c.getSuperclass(), aliases);
            for (final Class<?> i : c.getInterfaces()) {
                collect(i, aliases);
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Key) {
            final Key other = (Key) obj;
            return clazz.equals(other.clazz);
        }
        return false;
    }

    @Override
    public String toString() {
        return clazz.getName();
    }
}
