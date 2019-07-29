package jp.urgm.radishtainer.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MethodIterable implements Iterable<Method> {

    private final Class<?> clazz;

    public MethodIterable(final Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Iterator<Method> iterator() {
        final List<Method> methods = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            for (final Method method : c.getDeclaredMethods()) {
                if (method.isSynthetic() == false && isOverriden(methods, method) == false) {
                    methods.add(method);
                }
            }
        }
        return methods.iterator();
    }

    private static boolean isOverriden(final List<Method> methods, final Method method) {
        if (Modifier.isPrivate(method.getModifiers())) {
            return false;
        }
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        final Package pkg = method.getDeclaringClass().getPackage();
        for (final Method m : methods) {
            if (m.getDeclaringClass() != method.getDeclaringClass()
                    && m.getName().equals(method.getName())
                    && Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {

                if (Modifier.isPublic(method.getModifiers()) == false
                        && Modifier.isProtected(method.getModifiers()) == false
                        && Objects.equals(m.getDeclaringClass().getPackage(), pkg) == false) {
                    return false;
                }

                return true;
            }
        }
        return false;
    }
}
