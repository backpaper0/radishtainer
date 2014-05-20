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
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.hogedriven.backpaper0.radishtainer.event.Observes;

public class ClassInfo {

    private final List<Class<?>> classes = new ArrayList<>();
    private final List<Constructor<?>> constructors;
    private final List<Field> allFields = new ArrayList<>();
    private final List<Method> allMethods = new ArrayList<>();

    public ClassInfo(Class<?> clazz) {
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
        return constructors.stream()
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .findFirst()
                .orElseThrow(NoSuchMethodException::new);
    }

    public List<Field> getInjectableFields() {
        return allFields.stream()
                .filter(field -> isInjectable(field))
                .collect(Collectors.toList());
    }

    public List<Method> getInjectableMethods() {
        return allMethods.stream()
                .filter(method -> isInjectable(method))
                .collect(Collectors.toList());
    }

    public List<Method> getObservableMethods(Class<?> eventClass) {
        return allMethods.stream()
                .filter(method -> isObservableMethod(method, eventClass))
                .collect(Collectors.toList());
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
