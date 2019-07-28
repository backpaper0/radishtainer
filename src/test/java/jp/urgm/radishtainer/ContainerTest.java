package jp.urgm.radishtainer;

import static org.junit.Assert.*;

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

    private static class Aaa {
    }
}
