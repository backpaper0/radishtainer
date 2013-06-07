package net.hogedriven.backpaper0.radishtainer;

public abstract class Binding {

    public abstract Object getInstance(Container container);

    public abstract ClassInfo getClassInfo();

    public static Binding newClassBinding(Class<?> impl, Scope scope) {
        return new ClassBinding(impl, scope);
    }

    public static Binding newInstanceBinding(Object instance) {
        return new InstanceBinding(instance);
    }

    private static class ClassBinding extends Binding {

        private final Class<?> impl;

        private final Scope scope;

        public ClassBinding(Class<?> impl, Scope scope) {
            this.impl = impl;
            this.scope = scope;
        }

        @Override
        public Object getInstance(Container container) {
            return scope.getInstance(container, impl);
        }

        @Override
        public ClassInfo getClassInfo() {
            return new ClassInfo(impl);
        }
    }

    private static class InstanceBinding extends Binding {

        private final Object instance;

        public InstanceBinding(Object instance) {
            this.instance = instance;
        }

        @Override
        public Object getInstance(Container container) {
            container.inject(instance);
            return instance;
        }

        @Override
        public ClassInfo getClassInfo() {
            return new ClassInfo(instance.getClass());
        }
    }
}
