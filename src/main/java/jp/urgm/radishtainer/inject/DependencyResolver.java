package jp.urgm.radishtainer.inject;

import jp.urgm.radishtainer.Container;

public interface DependencyResolver {

    Object resolve(Container container);
}
