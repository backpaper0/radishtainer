package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.util.Objects;
import javax.inject.Singleton;

public class Descriptor<T> {

    private final Class<T> type;

    private final Annotation qualifier;

    //TODO private
    final Class<? extends T> impl;

    public Descriptor(Class<T> type, Annotation qualifier, Class<? extends T> impl) {
        this.type = type;
        this.qualifier = qualifier;
        this.impl = impl != null ? impl : type;
    }

    public boolean match(Class<?> type, Annotation qualifier) {
        return this.type == type && Objects.equals(this.qualifier, qualifier);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof Descriptor) == false) {
            return false;
        }
        Descriptor<?> other = (Descriptor<?>) obj;
        return Objects.equals(type, other.type)
                && Objects.equals(qualifier, other.qualifier)
                && Objects.equals(impl, other.impl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, qualifier, impl);
    }

    boolean isSingleton() {
        return impl.getAnnotation(Singleton.class) != null;
    }
}
