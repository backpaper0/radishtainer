package jp.urgm.radishtainer.inject.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import jp.urgm.radishtainer.inject.InjectionMember;

public interface InjectionMemberFactory {

	Optional<InjectionMember> fromMethod(Method method);

	Optional<InjectionMember> fromField(Field field);

}
