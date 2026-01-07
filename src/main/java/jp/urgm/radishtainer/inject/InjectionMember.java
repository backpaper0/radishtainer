package jp.urgm.radishtainer.inject;

import jp.urgm.radishtainer.Container;

public interface InjectionMember {

	Object inject(Container container, Object component);

}
