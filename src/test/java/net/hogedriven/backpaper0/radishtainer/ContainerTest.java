package net.hogedriven.backpaper0.radishtainer;

import net.hogedriven.backpaper0.radishtainer.test.Aaa;
import net.hogedriven.backpaper0.radishtainer.test.Bbb;
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

    @Test
    public void test_inject() throws Exception {
        Container c = newContainer();
        c.add(Aaa.class);
        Bbb target = new Bbb();
        c.inject(target);
        assertThat("private field", target.getPrivateField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("package private field", target.getPackagePrivateField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("protected field", target.getProtectedField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("public field", target.getPublicField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("private method", target.getPrivateMethod(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("package private method", target.getPackagePrivateMethod(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("protected method", target.getProtectedMethod(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("public method", target.getPublicMethod(), not(sameInstance(Aaa.INSTANCE)));
    }

    protected Container newContainer() {
        return new Container();
    }
}
