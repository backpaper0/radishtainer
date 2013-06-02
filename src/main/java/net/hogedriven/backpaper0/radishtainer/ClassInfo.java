package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassInfo {

    public List<List<Field>> allFields = new ArrayList<>();

    public List<List<Method>> allMethods = new ArrayList<>();

    public ClassInfo(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            classes.add(c);
        }
        Collections.reverse(classes);
        for (Class<?> c : classes) {
            allFields.add(Arrays.asList(c.getDeclaredFields()));
            List<Method> methods = new ArrayList<>();
            for (Method method : c.getDeclaredMethods()) {
                removeOverrided(allMethods, method);
                methods.add(method);
            }
            allMethods.add(methods);
        }
    }

    private void removeOverrided(List<List<Method>> allMethods, Method method) {
        for (List<Method> methods : allMethods) {
            for (Method other : methods) {
                if (isOverridden(method, other)) {
                    methods.remove(other);
                    return;
                }
            }
        }
    }

    static boolean isOverridden(Method method, Method other) {
        if (method.getDeclaringClass() == other.getDeclaringClass()) {
            return false;
        }
        if (other.getDeclaringClass().isAssignableFrom(method.getDeclaringClass()) == false) {
            return false;
        }
        if (Objects.equals(method.getName(), other.getName()) == false) {
            return false;
        }
        if (Arrays.equals(method.getParameterTypes(), other.getParameterTypes()) == false) {
            return false;
        }
        if (Modifier.isPrivate(other.getModifiers())) {
            return false;
        }
        if (Modifier.isProtected(other.getModifiers()) == false
                && Modifier.isPublic(other.getModifiers()) == false
                && Objects.equals(method.getDeclaringClass().getPackage(),
                other.getDeclaringClass().getPackage()) == false) {
            return false;
        }
        return true;
    }
}
