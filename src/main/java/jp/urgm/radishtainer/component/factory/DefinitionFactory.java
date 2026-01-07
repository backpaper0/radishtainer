package jp.urgm.radishtainer.component.factory;

import jp.urgm.radishtainer.component.Definition;

public interface DefinitionFactory {

	Definition create(Class<?> clazz);

}
