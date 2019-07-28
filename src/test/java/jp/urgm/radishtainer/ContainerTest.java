package jp.urgm.radishtainer;

import static org.junit.Assert.*;

import javax.inject.Singleton;

import org.junit.Test;

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

    private static class Aaa {
    }

    @Singleton
    private static class Bbb {
    }
}
