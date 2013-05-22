package net.hogedriven.backpaper0.radishtainer;

import net.hogedriven.backpaper0.radishtainer.test.Aaa;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContainerTest {

    @Test
    public void test_getInstance() throws Exception {
        Container c = newContainer();
        c.add(Aaa.class);
        Aaa instance = c.getInstance(Aaa.class);
        assertThat(instance, is(notNullValue()));
    }

    protected Container newContainer() {
        return new Container();
    }
}
