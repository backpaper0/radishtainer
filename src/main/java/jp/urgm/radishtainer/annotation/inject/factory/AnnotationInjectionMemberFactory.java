package jp.urgm.radishtainer.annotation.inject.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import javax.inject.Inject;

import jp.urgm.radishtainer.inject.InjectionMember;
import jp.urgm.radishtainer.inject.factory.InjectionMemberFactory;
import jp.urgm.radishtainer.inject.impl.DefaultInjectionField;
import jp.urgm.radishtainer.inject.impl.DefaultInjectionMethod;

public class AnnotationInjectionMemberFactory implements InjectionMemberFactory {

    @Override
    public Optional<InjectionMember> fromMethod(final Method method) {
        if (method.isAnnotationPresent(Inject.class) == false) {
            return Optional.empty();
        }
        final InjectionMember injectionMember = new DefaultInjectionMethod(method);
        return Optional.of(injectionMember);
    }

    @Override
    public Optional<InjectionMember> fromField(final Field field) {
        if (field.isAnnotationPresent(Inject.class) == false) {
            return Optional.empty();
        }
        final InjectionMember injectionMember = new DefaultInjectionField(field);
        return Optional.of(injectionMember);
    }
}
