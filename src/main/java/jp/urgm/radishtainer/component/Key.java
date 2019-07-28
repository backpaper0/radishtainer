package jp.urgm.radishtainer.component;

import java.util.Objects;

public class Key {

    private final Class<?> clazz;

    public Key(final Class<?> clazz) {
        this.clazz = clazz;
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
}
