package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.util.Objects;

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
}
