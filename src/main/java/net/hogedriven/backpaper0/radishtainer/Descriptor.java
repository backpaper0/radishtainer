package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.util.Objects;

public class Descriptor {

    private final Class<?> type;

    private final Annotation qualifier;

    public Descriptor(Class<?> type, Annotation qualifier) {
        this.type = type;
        this.qualifier = qualifier;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof Descriptor) == false) {
            return false;
        }
        Descriptor other = (Descriptor) obj;
        return Objects.equals(type, other.type)
                && Objects.equals(qualifier, other.qualifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, qualifier);
    }
}
