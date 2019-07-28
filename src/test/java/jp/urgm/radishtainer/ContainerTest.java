package jp.urgm.radishtainer;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;

import jp.urgm.radishtainer.container.ContainerBuilder;

public class ContainerTest {

    @Test
    public void getComponent() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Aaa.class)
                .build();
        final Aaa component = container.getComponent(Aaa.class);
        assertNotNull(component);
    }

    @Test
    public void getComponentPrototype() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Aaa.class)
                .build();
        final Aaa component1 = container.getComponent(Aaa.class);
        final Aaa component2 = container.getComponent(Aaa.class);
        assertNotNull(component1);
        assertNotSame(component1, component2);
    }

    @Test
    public void getComponentSingleton() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Bbb.class)
                .build();
        final Bbb component1 = container.getComponent(Bbb.class);
        final Bbb component2 = container.getComponent(Bbb.class);
        assertNotNull(component1);
        assertSame(component1, component2);
    }

    @Test
    public void constructorInjection() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Ccc1.class)
                .register(Ccc2.class)
                .build();
        final Ccc1 component1 = container.getComponent(Ccc1.class);
        final Ccc2 component2 = container.getComponent(Ccc2.class);
        assertNotNull(component2.component);
        assertSame(component1, component2.component);
    }

    @Test
    public void fieldInjection() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Ccc1.class)
                .register(Ccc3.class)
                .build();
        final Ccc1 component1 = container.getComponent(Ccc1.class);
        final Ccc3 component2 = container.getComponent(Ccc3.class);
        assertNotNull(component2.component);
        assertNull(component2.notInjected);
        assertSame(component1, component2.component);
    }

    @Test
    public void methodInjection() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Ccc1.class)
                .register(Ccc4.class)
                .build();
        final Ccc1 component1 = container.getComponent(Ccc1.class);
        final Ccc4 component2 = container.getComponent(Ccc4.class);
        assertNotNull(component2.component);
        assertSame(component1, component2.component);
    }

    @Test
    public void getComponentByInterface() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Ddd2.class)
                .build();
        final Ddd2 component1 = container.getComponent(Ddd2.class);
        final Ddd1 component2 = container.getComponent(Ddd1.class);
        assertSame(component1, component2);
    }

    @Test
    public void getComponentBySuperclass() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Eee2.class)
                .build();
        final Eee2 component1 = container.getComponent(Eee2.class);
        final Eee1 component2 = container.getComponent(Eee1.class);
        assertSame(component1, component2);
    }

    private static class Aaa {
    }

    @Singleton
    private static class Bbb {
    }

    @Singleton
    private static class Ccc1 {
    }

    private static class Ccc2 {

        Ccc1 component;

        @Inject
        Ccc2(final Ccc1 component) {
            this.component = component;
        }

        Ccc2(final Ccc1 component, final int forOverload) {
            throw new AssertionError();
        }
    }

    private static class Ccc3 {

        @Inject
        Ccc1 component;
        Ccc1 notInjected;
    }

    private static class Ccc4 {

        Ccc1 component;

        @Inject
        void method(final Ccc1 component) {
            this.component = component;
        }

        void notInjected(final Ccc1 component) {
            throw new AssertionError();
        }
    }

    private interface Ddd1 {
    }

    @Singleton
    private static class Ddd2 implements Ddd1 {
    }

    private static abstract class Eee1 {
    }

    @Singleton
    private static class Eee2 extends Eee1 {
    }
}
