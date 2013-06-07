package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
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

    private List<List<Field>> allFields = new ArrayList<>();

    private List<List<Method>> allMethods = new ArrayList<>();

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

    public List<List<Field>> getInjectableFields() {
        List<List<Field>> filtered = new ArrayList<>();
        for (List<Field> fields : allFields) {
            List<Field> list = new ArrayList<>();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class) && Modifier.isFinal(field.getModifiers()) == false) {
                    list.add(field);
                }
            }
            filtered.add(list);
        }
        return filtered;
    }

    public List<List<Method>> getInjectableMethods() {
        List<List<Method>> filtered = new ArrayList<>();
        for (List<Method> methods : allMethods) {
            List<Method> list = new ArrayList<>();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Inject.class) && Modifier.isAbstract(method.getModifiers()) == false) {
                    list.add(method);
                }
            }
            filtered.add(list);
        }
        return filtered;
    }

    public List<List<Method>> getObservableMethods(Class<?> eventClass) {
        List<List<Method>> filtered = new ArrayList<>();
        for (List<Method> methods : allMethods) {
            List<Method> list = new ArrayList<>();
            for (Method method : methods) {
                if (isObservableMethod(method, eventClass)) {
                    list.add(method);
                }
            }
            filtered.add(list);
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
