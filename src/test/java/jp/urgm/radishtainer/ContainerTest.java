package jp.urgm.radishtainer;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
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

    @Test
    public void qualifier() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Fff2.class)
                .register(Fff3.class)
                .build();
        final Fff1 component1 = container.getComponent(Fff1.class, new NamedImpl("foo"));
        final Fff1 component2 = container.getComponent(Fff1.class, new NamedImpl("bar"));
        final Fff2 component3 = container.getComponent(Fff2.class);
        final Fff3 component4 = container.getComponent(Fff3.class);
        assertSame(component3, component1);
        assertSame(component4, component2);
    }

    @Test
    public void qualifierGetComponentByType() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Fff2.class)
                .build();
        final Fff1 component1 = container.getComponent(Fff1.class, new NamedImpl("foo"));
        final Fff1 component2 = container.getComponent(Fff1.class);
        final Fff2 component3 = container.getComponent(Fff2.class);
        assertSame(component3, component1);
        assertSame(component3, component2);
    }

    @Test
    public void constructorInjectionWithQualifier() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Ggg2.class)
                .register(Ggg3.class)
                .register(Ggg4.class)
                .build();
        final Ggg2 component1 = container.getComponent(Ggg2.class);
        final Ggg3 component2 = container.getComponent(Ggg3.class);
        final Ggg4 component3 = container.getComponent(Ggg4.class);
        assertSame(component1, component3.component1);
        assertSame(component2, component3.component2);
    }

    @Test
    public void fieldInjectionWithQualifier() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Ggg2.class)
                .register(Ggg3.class)
                .register(Ggg5.class)
                .build();
        final Ggg2 component1 = container.getComponent(Ggg2.class);
        final Ggg3 component2 = container.getComponent(Ggg3.class);
        final Ggg5 component3 = container.getComponent(Ggg5.class);
        assertSame(component1, component3.component1);
        assertSame(component2, component3.component2);
    }

    @Test
    public void methodInjectionWithQualifier() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Ggg2.class)
                .register(Ggg3.class)
                .register(Ggg6.class)
                .build();
        final Ggg2 component1 = container.getComponent(Ggg2.class);
        final Ggg3 component2 = container.getComponent(Ggg3.class);
        final Ggg6 component3 = container.getComponent(Ggg6.class);
        assertSame(component1, component3.component1);
        assertSame(component2, component3.component2);
    }

    @Test
    public void constructorInjectionAsProvider() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Hhh1.class)
                .register(Hhh2.class)
                .build();
        final Hhh1 component1 = container.getComponent(Hhh1.class);
        final Hhh2 component2 = container.getComponent(Hhh2.class);
        assertNotNull(component2.provider);
        assertSame(component1, component2.provider.get());
    }

    @Test
    public void fieldInjectionAsProvider() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Hhh1.class)
                .register(Hhh3.class)
                .build();
        final Hhh1 component1 = container.getComponent(Hhh1.class);
        final Hhh3 component2 = container.getComponent(Hhh3.class);
        assertNotNull(component2.provider);
        assertSame(component1, component2.provider.get());
    }

    @Test
    public void methodInjectionAsProvider() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Hhh1.class)
                .register(Hhh4.class)
                .build();
        final Hhh1 component1 = container.getComponent(Hhh1.class);
        final Hhh4 component2 = container.getComponent(Hhh4.class);
        assertNotNull(component2.provider);
        assertSame(component1, component2.provider.get());
    }

    @Test
    public void fieldInjectionSuperclass() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Iii1.class)
                .register(Iii3.class)
                .build();
        final Iii1 component1 = container.getComponent(Iii1.class);
        final Iii3 component2 = container.getComponent(Iii3.class);
        assertSame(component1, component2.component1);
        assertSame(component1, component2.component2);
    }

    @Test
    public void methodInjectionSuperclass() throws Exception {
        final Container container = new ContainerBuilder()
                .register(Iii1.class)
                .register(Iii5.class)
                .build();
        final Iii1 component1 = container.getComponent(Iii1.class);
        final Iii5 component2 = container.getComponent(Iii5.class);
        assertSame(component1, component2.component1);
        assertSame(component1, component2.component2);
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

    private interface Fff1 {
    }

    @Named("foo")
    @Singleton
    private static class Fff2 implements Fff1 {
    }

    @Named("bar")
    @Singleton
    private static class Fff3 implements Fff1 {
    }

    private interface Ggg1 {
    }

    @Named("foo")
    @Singleton
    private static class Ggg2 implements Ggg1 {
    }

    @Named("bar")
    @Singleton
    private static class Ggg3 implements Ggg1 {
    }

    private static class Ggg4 {

        Ggg1 component1;
        Ggg1 component2;

        @Inject
        Ggg4(@Named("foo") final Ggg1 component1, @Named("bar") final Ggg1 component2) {
            this.component1 = component1;
            this.component2 = component2;
        }
    }

    private static class Ggg5 {

        @Inject
        @Named("foo")
        Ggg1 component1;
        @Inject
        @Named("bar")
        Ggg1 component2;
    }

    private static class Ggg6 {

        Ggg1 component1;
        Ggg1 component2;

        @Inject
        void method(@Named("foo") final Ggg1 component1, @Named("bar") final Ggg1 component2) {
            this.component1 = component1;
            this.component2 = component2;
        }
    }

    @Singleton
    private static class Hhh1 {
    }

    private static class Hhh2 {

        Provider<Hhh1> provider;

        @Inject
        Hhh2(final Provider<Hhh1> provider) {
            this.provider = provider;
        }
    }

    private static class Hhh3 {

        @Inject
        Provider<Hhh1> provider;
    }

    private static class Hhh4 {

        Provider<Hhh1> provider;

        @Inject
        void method(final Provider<Hhh1> provider) {
            this.provider = provider;
        }
    }

    @Singleton
    private static class Iii1 {
    }

    private static class Iii2 {
        @Inject
        Iii1 component1;
    }

    private static class Iii3 extends Iii2 {
        @Inject
        Iii1 component2;
    }

    private static class Iii4 {

        Iii1 component1;

        @Inject
        void setComponent1(final Iii1 component1) {
            this.component1 = component1;
        }
    }

    private static class Iii5 extends Iii4 {

        Iii1 component2;

        @Inject
        void setComponent2(final Iii1 component2) {
            this.component2 = component2;
        }
    }
}
