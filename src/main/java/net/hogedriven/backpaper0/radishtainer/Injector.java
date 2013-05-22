package net.hogedriven.backpaper0.radishtainer;

public abstract class Injector {

    public abstract boolean isInjectable();

    public abstract void inject(Container container, Object target);
}
