package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import net.hogedriven.backpaper0.radishtainer.event.Observes;

public class ClassInfo {

    private List<Class<?>> classes;
    private List<Constructor<?>> constructors;
    private List<Field> allFields = new ArrayList<>();
    private List<Method> allMethods = new ArrayList<>();

    public ClassInfo(Class<?> clazz) {
        classes = new ArrayList();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            classes.add(c);
        }
        constructors = Arrays.asList(clazz.getDeclaredConstructors());
        Collections.reverse(classes);
        for (Class<?> c : classes) {
            allFields.addAll(Arrays.asList(c.getDeclaredFields()));
            for (Method method : c.getDeclaredMethods()) {
                removeOverrided(allMethods, method);
                allMethods.add(method);
            }
        }
    }

    public List<Class<?>> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    public List<Constructor<?>> getInjectableConstructors() {
        List<Constructor<?>> filtered = new ArrayList<>();
        for (Constructor<?> constructor : constructors) {
            if (isInjectable(constructor)) {
                filtered.add(constructor);
            }
        }
        return filtered;
    }

    public Constructor<?> getDefaultConstructor() throws NoSuchMethodException {
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterTypes().length == 0) {
                return constructor;
            }
        }
        throw new NoSuchMethodException();
    }

    public List<Field> getInjectableFields() {
        List<Field> filtered = new ArrayList<>();
        for (Field field : allFields) {
            if (isInjectable(field)) {
                filtered.add(field);
            }
        }
        return filtered;
    }

    public List<Method> getInjectableMethods() {
        List<Method> filtered = new ArrayList<>();
        for (Method method : allMethods) {
            if (isInjectable(method)) {
                filtered.add(method);
            }
        }
        return filtered;
    }

    public List<Method> getObservableMethods(Class<?> eventClass) {
        List<Method> filtered = new ArrayList<>();
        for (Method method : allMethods) {
            if (isObservableMethod(method, eventClass)) {
                filtered.add(method);
            }
        }
        return filtered;
    }

    private boolean isObservableMethod(Method method, Class<?> eventClass) {
        Class<?>[] types = method.getParameterTypes();
        if (types.length > 0 && method.getParameterTypes()[0] == eventClass) {
            for (Annotation annotation : method.getParameterAnnotations()[0]) {
                if (annotation.annotationType() == Observes.class) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeOverrided(List<Method> allMethods, Method method) {
        for (Method other : allMethods) {
            if (isOverridden(method, other)) {
                allMethods.remove(other);
                return;
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

    static boolean isInjectable(Field field) {
        return field.isAnnotationPresent(Inject.class) && Modifier.isFinal(field.getModifiers()) == false;
    }

    static boolean isInjectable(Method method) {
        return method.isAnnotationPresent(Inject.class) && Modifier.isAbstract(method.getModifiers()) == false;
    }

    static boolean isInjectable(Constructor<?> constructor) {
        return constructor.isAnnotationPresent(Inject.class);
    }
}
